#ifndef JVMLAUNCHER_H
#define JVMLAUNCHER_H

#include "JVMProperties.h"
#include <jni.h>

class JVMLauncher {
  public:
    JVMLauncher( const JVMProperties* launch_props );
    virtual ~JVMLauncher();
    bool LaunchJVM();

  protected:
    typedef jint (JNICALL *CreateJavaVM_t)(JavaVM **pvm, void **env, void *args);
    typedef jint (JNICALL *GetDefaultJavaVMInitArgs_t)(void *args);
    typedef struct {
      CreateJavaVM_t CreateJavaVM;
      GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs;
    } InvocationFunctions;

    const JVMProperties *m_properties;
    HINSTANCE m_hJavaInstance;
    JavaVM* m_main_jvm;

    void ErrorMsg(std::string msg);
    bool InvokeMain(JNIEnv* env);
    JNIEnv* CreateVM();
    HINSTANCE LoadJVM(InvocationFunctions *ifn);
};

#endif