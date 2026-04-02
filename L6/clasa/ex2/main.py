class BaseClass:
    num_base_calls=0

    def call_me(self,caller):
        print("Apel met din BaseClass,caller=",caller)
        self.num_base_calls+=1

class LeftSubClass(BaseClass):
    num_left_calls=0

    def call_me(self,caller):
        print("Apel met din LeftSubClass,caller=",caller)
        #super().call_me("LeftSubClass") cu mro
        BaseClass.call_me(self,"LeftSubClass")
        self.num_left_calls+=1

class RightSubClass(BaseClass):
    num_right_calls=0

    def call_me(self,caller):
        print("Apel met din RightSubClass,caller=",caller)
        #super().call_me("RightSubClass")
        BaseClass.call_me(self,"RightSubClass")
        self.num_right_calls+=1

class SubClass(LeftSubClass,RightSubClass):
    num_sub_calls=0

    def call_me(self,caller):
        print("Apel met din SubClass,caller=",caller)
        #super().call_me("SubClass")
        #super().call_me("SubClass")
        BaseClass.call_me(self,"SubClass")
        BaseClass.call_me(self,"SubClass")
        self.num_sub_calls+=1

if __name__=='__main__':
    subClass_instance= SubClass()
    #print(SubClass.__mro__) #met resolution order
    #ordinea in care python cauta o metoda
    #super=urmatoarea clasa din lista mro
    subClass_instance.call_me("__main__")
    print(subClass_instance.num_sub_calls,
        subClass_instance.num_left_calls,
        subClass_instance.num_right_calls,
        subClass_instance.num_base_calls)
    
'''
(<class '__main__.SubClass'>, <class '__main__.LeftSubClass'>, <class '__main__.RightSubClass'>, <class '__main__.BaseClass'>, <class 'object'>)
Apel met din SubClass,caller= __main__
Apel met din LeftSubClass,caller= SubClass
Apel met din RightSubClass,caller= LeftSubClass
Apel met din BaseClass,caller= RIghtSubClass
Apel met din LeftSubClass,caller= SubClass
Apel met din RightSubClass,caller= LeftSubClass
Apel met din BaseClass,caller= RIghtSubClass
1 2 2 2
OUTPUT exemplu de la prof cu super() care foloseste mro,viziteaza tot lantul


Apel met din SubClass,caller= __main__
Apel met din BaseClass,caller= SubClass
Apel met din BaseClass,caller= SubClass
1 0 0 2
OUTPUT cu BaseClass.call_me(..),asta sare direct la tinta
1 subclass
0 left,right
2 baseClass
left,right sare direct la bunicu'
'''