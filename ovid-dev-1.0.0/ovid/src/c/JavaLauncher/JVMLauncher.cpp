#include "stdafx.h"
#include <vector>
#include <algorithm>
#include "JVMProperties.h"
#include "JVMLauncher.h"

typedef std::vector< std::string > lines_t;

JVMLauncher::JVMLauncher( const JVMProperties* launch_props )
            : m_properties( launch_props ), m_hJavaInstance( 0 )
{
}

JVMLauncher::~JVMLauncher()
{
  delete m_properties;
}

// -----------------------------------------------
// LoadJVM
//
// Load the Java Virtual Machine and obtain
// the address  of the JNI_CreateJavaVM method.
//
// Arguments:
//
// ifn - address of invocation functions structure
//
// Return:
//
// JVM DLL instance on success, NULL on error
// -----------------------------------------------

HINSTANCE JVMLauncher::LoadJVM(InvocationFunctions *ifn)
{
  HKEY key;
  HINSTANCE hinstance;
  static BYTE DLLdata[128];
  DWORD dwCount = 128, dwType;
  BYTE szval[256];
  BYTE szversion[128];
  BOOL homeFound=FALSE;
  memset(&szversion,0,dwCount);
  if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, "SOFTWARE\\JavaSoft\\Java Runtime Environment", 0, KEY_ALL_ACCESS,&key)
      != ERROR_SUCCESS)
    return NULL;
  //Get current version
  if (RegQueryValueEx(key, "CurrentVersion", NULL, &dwType, (LPBYTE)&szversion, &dwCount) != ERROR_SUCCESS)
  {
    RegCloseKey(key);
    return NULL;
  }
  dwCount=128;
  RegCloseKey(key);
  sprintf((char*)&szval,"SOFTWARE\\JavaSoft\\Java Runtime Environment\\%s",szversion);
  if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, (char*)&szval, 0, KEY_ALL_ACCESS, &key) != ERROR_SUCCESS)
  {
    return NULL;
  }
  // We need to obtain the value of RuntimeLib.  This value is the complete
  // directory path and name of the JVM 1.2/2.0 DLL.

  dwCount=128;
  if (RegQueryValueEx(key, "RuntimeLib", NULL, &dwType, (LPBYTE)&DLLdata, &dwCount) != ERROR_SUCCESS)
  {
    RegCloseKey(key);
    return NULL;
  }
  dwCount=128;
  if (RegQueryValueEx(key, "JavaHome", NULL, &dwType, (LPBYTE)&szval, &dwCount) == ERROR_SUCCESS)
  {
    homeFound=TRUE;
  }

  RegCloseKey(key);

  //try to find a server jvm
  if (homeFound) {
    strcat((char*)&szval,"\\bin\\server\\jvm.dll");
    hinstance = LoadLibrary((LPCTSTR) szval);
  }
  if (hinstance!=NULL || (hinstance = LoadLibrary((LPCTSTR) DLLdata)) != NULL)
  {
    // Since the DLL has now been successfully loaded, we need to obtain
    // the entry point address of the JNI_CreateJavaVM function.

    ifn->CreateJavaVM = (CreateJavaVM_t)GetProcAddress(hinstance, "JNI_CreateJavaVM");
    ifn->GetDefaultJavaVMInitArgs = (GetDefaultJavaVMInitArgs_t)GetProcAddress(hinstance, "JNI_GetDefaultJavaVMInitArgs");

    // If we cannot obtain the JNI_CreateJavaVM function, we cannot create
    // a JVM so there is no point in continuing.

    if (ifn->CreateJavaVM == NULL)
    {
      FreeLibrary(hinstance);
      return NULL;
    }
  }

  return hinstance;
}

JNIEnv* JVMLauncher::CreateVM()
{
  JavaVM* jvm;
  JNIEnv* env;
  jint ret;
  JavaVMInitArgs args;
  JavaVMOption* options;;
  lines_t lines;
  jint i=0;
  jint len;
  InvocationFunctions ifn;
  HINSTANCE inst=LoadJVM(&ifn);
  memset(&args, 0, sizeof(JavaVMInitArgs));
  if (inst==NULL) {
    ErrorMsg("Could not find/load a Java Virtual Machine.\n");
    return NULL;
  }
  args.version = JNI_VERSION_1_2;
  for (std::vector<std::string>::const_iterator iter = m_properties->GetVMArgs()->begin();
       iter !=  m_properties->GetVMArgs()->end();
       ++iter )
  {
    lines.push_back( *iter );
  }
  lines.push_back( std::string("-Djava.class.path=")+m_properties->GetClasspath() );
  len = (jint)lines.size();
  options=new JavaVMOption[len];
  char *sz;
  while(i<len) {
    sz=(char*)lines[i].c_str();
    options[i++].optionString=sz;
  }
  args.options = options;
  args.nOptions = len;
  args.ignoreUnrecognized = JNI_TRUE;
  ret=ifn.CreateJavaVM(&jvm, (void **)&env, &args);
  delete [] options;
  if (jvm==NULL)
  {
    FreeLibrary(inst);
    ErrorMsg("Could not create a Java Virtual Machine.\n");
    return NULL;
  }
  m_hJavaInstance = inst;
  return env;
}

bool JVMLauncher::InvokeMain(JNIEnv* env)
{
  jclass serverClass=NULL;
  jclass stringClass=NULL;
  jmethodID mainMethod=NULL;
  jobjectArray applicationArgs=NULL;
  bool ret_val = false;
  env->GetJavaVM(&m_main_jvm);
  std::string main_class_path( m_properties->GetMainClass() );
  std::replace(main_class_path.begin(), main_class_path.end(), '.', '/' );
  serverClass = env->FindClass(main_class_path.c_str());
  if (serverClass==NULL)
  {
    ErrorMsg(std::string("Could not find ") + m_properties->GetMainClass() + " class.\n");
  } else {
    mainMethod = env->GetStaticMethodID(serverClass,
                                        m_properties->GetEntryMethod().c_str(),
                                        m_properties->GetEntrySignature().c_str());
    if (mainMethod==NULL)
    {
      ErrorMsg(std::string("Could not find ") + m_properties->GetMainClass() + " " +
               m_properties->GetEntryMethod() + "() method\n");
    } else {
      stringClass = env->FindClass("java/lang/String");
      applicationArgs = env->NewObjectArray(0, stringClass, NULL);
      env->CallStaticObjectMethod(serverClass, mainMethod, applicationArgs);
      if (env->ExceptionOccurred())
      {
        env->ExceptionDescribe();
      } else {
        ret_val = true;
        m_main_jvm->DetachCurrentThread();
      }
    }
  }
  return ret_val;
}

bool JVMLauncher::LaunchJVM()
{
  JNIEnv* env = CreateVM();
  if (env==NULL) {
    return false;
  }
  if (env==NULL) {
    return false;
  }
  bool ret_val = InvokeMain( env );
  if (m_hJavaInstance!=NULL)
  {
    FreeLibrary(m_hJavaInstance);
    m_hJavaInstance=NULL;
  }
  return ret_val;
}

void JVMLauncher::ErrorMsg(std::string msg)
{
  MessageBox(0, (char*)msg.c_str(), "Startup Error", MB_ICONINFORMATION|MB_OK);
}
