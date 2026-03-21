
def citire():
    print("nr aruncari ale unei monede(n)=")
    n=int(input())
    print("nr (x)=")
    x=int(input())
    if n<1 or x<1 or x>n:
        raise ValueError("reintrodu n,x")
        #raise echivalent cu throw in c++
    return n,x

def ex3(n,x):
    from scipy.stats import binom
    #trb cdf ca sa fie cummulative(suma)
    #probabilitatea sa dea pajura e 0.5
    print(f"Probabilitatea de a obține cel mult {x} pajuri din {n} aruncări:{binom.cdf(x,n,0.5):.4f}")


def main():
    n,x=citire()
    ex3(n,x)

if __name__=="__main__":
    main()