from __future__ import annotations
#ca sa pot folosi nnumele clasei in interiorul ei
#fara sa se planga bossu
from abc import ABC, abstractmethod
from typing import Optional
import subprocess
import glob
import shutil
import os
#pt wildcard
#lantul de responsab
class Handler(ABC):
    @abstractmethod
    def set_next(self,handler:Handler)->Handler:
        pass

    @abstractmethod
    def handle(self,content:str)->Optional[str]:
        #este posibibil sa ret str sau nimic
        pass

class AbstractHandler(Handler):
    def __init__(self):
        self._next_handler:Optional[Handler]=None

    def set_next(self, handler)->Handler:
        self._next_handler=handler
        return handler
    
    def handle(self,content:str)->Optional[Command]:
        if self._next_handler:
            return self._next_handler.handle(content)
        return None
    
class PythonHandler(AbstractHandler):
    def handle(self,file:str)->Optional[Command]:
        try:
            with open(file,'r') as f:
                content=f.read()
                if "def" in content or "__main__" in content:
                    return ExecutePython(file)
        except Exception as e:
            print(f"err desc fisier python {e}")
        return super().handle(file)
     
class KotlinHandler(AbstractHandler):
    def handle(self,file:str)->Optional[Command]:
        try:
            with open(file,'r') as f:
                content=f.read()
                if "fun" in content or "var" in content or "val" in content:
                    return ExecuteKotlin(file)
        except Exception as e:
            print(f"err desc fisier kotlin {e}")
        return super().handle(file)
    
class BashHandler(AbstractHandler):
    def handle(self,file:str)->Optional[Command]:
        try:
            with open(file,'r') as f:
                content=f.read()
                if "#!/bin/bash" in content:
                    return ExecuteBash(file)
        except Exception as e:
            print(f"err desc fisier Bash {e}")
        return super().handle(file)
        
class JavaHandler(AbstractHandler):
    def handle(self,file:str)->Optional[Command]:
        try:
            with open(file,'r') as f:
                content=f.read()
                if "static void main(String[] args)" in content:
                    return ExecuteJava(file)
        except Exception as e:
            print(f"err desc fisier java {e}")
        return super().handle(file)
        
#comanda
class Command(ABC):
    @abstractmethod
    def execute(self)->Optional[str]:
        pass

class ExecutePython(Command):
    def __init__(self,file:str):
        self.file=file

    def execute(self)->Optional[str]:
        try:
            result=subprocess.run(
                ["python3",self.file],
                capture_output=True,
                text=True
            )
            return result.stdout if result.returncode==0 else result.stderr
        except Exception as e:
            return f"eroare exec python {e}"
        
class ExecuteBash(Command):
    def __init__(self,file:str):
        self.file=file

    def execute(self)->Optional[str]:
        try:
            result=subprocess.run(
                ["bash",self.file],
                capture_output=True,
                text=True
            )
            return result.stdout if result.returncode==0 else result.stderr
        except Exception as e:
            return f"eroare exec bash {e}"
        
class ExecuteJava(Command):
    def __init__(self,file:str):
        self.file=file

    def execute(self)->Optional[str]:
        try:
            #la java trb compilare si rulare
            #javac accepta doar cu .java la final
            #trb facuta o copie
            copie=self.file+".java"
            shutil.copy(self.file,copie)
            #compile
            compile_result=subprocess.run(
                ["javac",copie],
                capture_output=True,
                text=True
            )
            if compile_result.returncode != 0:
                return f"err compilare java {compile_result.stderr}"
            pt_rulare=os.path.basename(copie).replace(".java","")
            result=subprocess.run(
                ["java",pt_rulare],
                capture_output=True,
                text=True
            )
            if os.path.exists(copie):
                os.remove(copie)
            if os.path.exists(pt_rulare+".class"):
                os.remove(pt_rulare+".class")
            return result.stdout if result.returncode==0 else result.stderr
        except Exception as e:
            return f"eroare exec Java {e}"
        
class ExecuteKotlin(Command):
    def __init__(self,file:str):
        self.file=file

    def execute(self)->Optional[str]:
        try:
            #la java trb compilare si rulare
            #javac accepta doar cu .java la final
            #trb facuta o copie
            copie=self.file+".kt"
            output_jar=self.file+".jar"
            shutil.copy(self.file,copie)
            #compile
            compile_result=subprocess.run(
                ["kotlinc",copie,"-include-runtime", "-d", output_jar],
                capture_output=True,
                text=True
            )
            if compile_result.returncode != 0:
                return f"err compilare Kotlin {compile_result.stderr}"
            result=subprocess.run(
                ["java","-jar",output_jar],
                capture_output=True,
                text=True
            )
            os.remove(copie)
            os.remove(output_jar)
            return result.stdout if result.returncode==0 else result.stderr
        except Exception as e:
            return f"eroare exec Kotlin {e}"
  
if __name__=='__main__':
    python_handler = PythonHandler()
    kotlin_handler = KotlinHandler()
    bash_handler = BashHandler()
    java_handler = JavaHandler()
    
    python_handler.set_next(kotlin_handler).set_next(bash_handler).set_next(java_handler)
    file=input('da fisier:')
    print(python_handler.handle(file).execute())
    
    