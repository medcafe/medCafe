#ifndef JVMPROPERTIES_H
#define JVMPROPERTIES_H

#include <string>
#include <vector>

class JVMProperties {
  public:
    JVMProperties(){};
    virtual ~JVMProperties(){};
    virtual const std::string GetMainClass() const = 0;
    virtual const std::vector< std::string > *GetVMArgs() const = 0;
    virtual const std::string GetClasspath() const = 0;
    virtual const std::string GetEntryMethod() const = 0;
    virtual const std::string GetEntrySignature() const = 0;
};

#endif