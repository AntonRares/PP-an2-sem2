import os
#clase
#refactoring guru
class GenericFile:
    def get_path(self):
        raise NotImplementedError("mesaj")
    def get_freq(self):
        raise NotImplementedError("mesaj")
    def getTotalCaractere(self):
        raise NotImplementedError("mesaj")
    def isValid(Self):
         raise NotImplementedError("mesaj")
    
class TextASCII(GenericFile):
    def __init__(self,path_absolut):
        self.path_absolut=os.path.abspath(path_absolut)
        self.frecvente=self.get_freq()
        self.caractere=self.getTotalCaractere()

    def get_path(self):
        return self.path_absolut
    
    def get_freq(self):
        frecv=[0]*256
        try:
            with open(self.path_absolut,'rb')as f:
                bloc=f.read(4096)
                if not bloc:
                    return [0]*256
                for octet in bloc:
                    frecv[octet]+=1
        except Exception as e:
            print(f"err desc fisier {e}")
            return None
        return frecv
    
    def getTotalCaractere(self):
        return(sum(self.frecvente))
    
    def isValid(self):
        if self.caractere==0:
            return False
        nr=sum(self.frecvente[i] for i in range(32,128))+self.frecvente[9]+self.frecvente[10]+self.frecvente[13]
        return (nr/self.caractere)>0.9
    
class TextUNICODE(GenericFile):
    def __init__(self,path_absolut):
        self.path_absolut=os.path.abspath(path_absolut)
        self.frecvente=self.get_freq()
        self.caractere=self.getTotalCaractere()

    def get_path(self):
        return self.path_absolut
    
    def get_freq(self):
        frecv=[0]*256
        try:
            with open(self.path_absolut,'rb')as f:
                bloc=f.read(4096)
                if not bloc:
                    return [0]*256
                for octet in bloc:
                    frecv[octet]+=1
        except Exception as e:
            print(f"err desc fisier {e}")
            return None
        return frecv
    
    def getTotalCaractere(self):
        return(sum(self.frecvente))
    
    def isValid(self):
        if self.caractere == 0:
            return False
        return (self.frecvente[0]/self.caractere) >=0.3

class Binary(GenericFile):
    def __init__(self,path_absolut):
        self.path_absolut=os.path.abspath(path_absolut)
        self.frecvente=self.get_freq()

    def get_path(self):
        return self.path_absolut
    
    def get_freq(self):
        frecv=[0]*256
        try:
            with open(self.path_absolut,'rb')as f:
                bloc=f.read(4096)
                if not bloc:
                    return [0]*256
                for octet in bloc:
                    frecv[octet]+=1
        except Exception as e:
            print(f"err desc fisier {e}")
            return None
        return frecv
    
    def getTotalCaractere(self):
        return(sum(self.frecvente))
    
    def isValid(self):
        return 
        #n am instalat libraria :)
        '''
        from scipy.stats import chisquare
        if self.getTotalCaractere() == 0:
            return False
        if(len(self.frecvente)) <=1:
            return False
        stat,p_value=chisquare(self.frecvente)
        #algoritm pentru testarea uniformitatii
        #pvalue mare daca distr unifrom
        #0.05 prag
        return p_value > 0.05
        '''
            
class XMLFile(TextASCII):
    def __init__(self, path_absolut):
        super().__init__(path_absolut)
        self.first_tag=self.get_first_tag()

    def get_first_tag(self):
        try:
            with open(self.path_absolut,'r')as f:
                content=f.read().strip()
                if not content:
                    return ''
                start=content.find('<')
                end=content.find('>')
                if start != -1 and end !=-1:
                    return content[start+1:end]
        except Exception as e:
            print(f"err desc fisier {e}")
        return None
    
    def isValid(self):
        return super().isValid() and self.first_tag != ""

class BMP(Binary):
    def __init__(self, path_absolut):
        super().__init__(path_absolut)
        self.width=0
        self.height=0
        self.bpp=0
        self.read_info()
    
    def isValid(self):
        try:
            with open(self.path_absolut, 'rb') as f:
                return f.read(2) == b'BM'#asa incepe fisier bmp
        except:
            return False
        
    def read_info(self):
        if not self.isValid():
            return 
        try:
            with open(self.path_absolut, 'rb') as f:
                header = f.read(54)
                #bmp stocheaza in little endian
                self.width = int.from_bytes(header[18:22], 'little')
                self.height = int.from_bytes(header[22:26], 'little')
                self.bpp = int.from_bytes(header[28:30], 'little')
        except Exception as e:
            print(f"err desc fisier {e}")


    def show_info(self):
        print("show: ",self.width,' ',self.height,' ',self.bpp)
#script
def script(path):
    for root,subdirs,files in os.walk(os.path.abspath(path)):
        print('-------')
        print("root:",root)
        for file in files:
            full_path=os.path.join(root,file)
            '''
            asa merge, doar ca e ineficient
            bmp = BMP(full_path)
            xml = XMLFile(full_path)
            ascii_file = TextASCII(full_path)
            unicode_file = TextUNICODE(full_path)
            if bmp.isValid():
                print("BMP:", full_path)
                bmp.show_info()
            elif xml.isValid():
                print("XML ASCII:", full_path, "tag:", xml.get_first_tag())
            elif unicode_file.isValid():
                print("UNICODE:", full_path)
            elif ascii_file.isValid():
                print("ASCII:", full_path)
            else:
                print("BINARY:", full_path)
            '''
            #asa mai bine ca sa nu creez obj inutil
            bmp = BMP(full_path)
            if bmp.isValid():
                print("BMP:", full_path)
                bmp.show_info()

            else:
                xml = XMLFile(full_path)
                if xml.isValid():
                    print("XML ASCII:", full_path, "tag:", xml.get_first_tag())

                else:
                    unicode_file = TextUNICODE(full_path)
                    if unicode_file.isValid():
                        print("UNICODE:", full_path)

                    else:
                        ascii_file = TextASCII(full_path)
                        if ascii_file.isValid():
                            print("ASCII:", full_path)

                        else:
                            print("BINARY:", full_path)
#main
if __name__=='__main__':
    print('da cale')
    path=input()
    script(path)
'''
PS D:\facultate\an2\sem2\pp\lab6\acasa> python3 main.py
da cale
D:/facultate/an2/sem2/pa/recapTest1/
-------
root: D:\facultate\an2\sem2\pa\recapTest1
XML ASCII: D:\facultate\an2\sem2\pa\recapTest1\f.cpp tag: iostream
XML ASCII: D:\facultate\an2\sem2\pa\recapTest1\f.h tag: None
XML ASCII: D:\facultate\an2\sem2\pa\recapTest1\main.cpp tag: iostream
err desc fisier 'charmap' codec can't decode byte 0x90 in position 2: character maps to <undefined>
UNICODE: D:\facultate\an2\sem2\pa\recapTest1\main.exe
'''