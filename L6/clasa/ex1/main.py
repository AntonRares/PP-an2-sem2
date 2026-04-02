class Contact:
    def __init__(self,name,email):
        self.name=name
        self.email=email
    
    def __repr__(self):
        return "Contact ({},{})".format(self.name,self.email)
    
class Friend(Contact):
    def __init__(self, name, email,phone):#constr
        super().__init__(name, email)
        self.phone=phone

    def __repr__(self):
        return "Contact ({},{},{})".format(self.name,self.email,self.phone)
    
class ContactAgend():
    def __init__(self):
        self.contacts=[]
    '''
    def addContact(self,contact):
        try: 
            self.contacts.append(contact)
        except:
            print("can t add contact")

    #append pe lista aproape niciodata err
    '''

    def addContact(self, contact):
        if isinstance(contact, Contact):
            self.contacts.append(contact)
        else:
            print("can t add contact")
    

    def showContactAgend(self):
        print("showContactAgend:")
        for c in self.contacts:
            print(c)#se cauta automat repr pt obiectul curent

if __name__=='__main__':
    #creez agenda
    agenda= ContactAgend()
    #instantiez
    c1 = Contact("Andre", "andrei@gmail.com")
    f1 = Friend("Maria", "maria@gmail.com", "0711111111")
    #adaug in agenda
    agenda.addContact(c1)
    agenda.addContact(f1)
    #afisare agenda
    agenda.showContactAgend()

