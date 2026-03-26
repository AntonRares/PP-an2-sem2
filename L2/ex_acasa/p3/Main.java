package org.example;
import org.graalvm.polyglot.*;
public class Main
{

    static void main(String[] args)
    {

        try(Context polyglot = Context.newBuilder().option("engine.WarnInterpreterOnly","false").allowAllAccess(true).build())
        {
            polyglot.eval("python", """
                    def citire():
                        print("nr aruncari ale unei monede(n)=")
                        n=int(input())
                        print("nr (x)=")
                        x=int(input())
                        if n<1 or x<1 or x>n:
                            raise ValueError("reintrodu n,x")
                            #raise echivalent cu throw in c++
                        return n,x
                    """);
            Value citire=polyglot.getBindings("python").getMember("citire");
            Value rez=citire.execute();
            int n=rez.getArrayElement(0).asInt();
            int x=rez.getArrayElement(1).asInt();

            polyglot.eval("python", """
                    import math
                    
                    def ex3(n, x):
                        def comb(n, k):
                            return math.comb(n, k)
                    
                        p = 0
                        for k in range(x+1):
                            p += comb(n, k) * (0.5**k) * (0.5**(n-k))
                    
                        print(f"Probabilitatea: {p:.4f}")
                    """);
            Value ex3=polyglot.getBindings("python").getMember("ex3");
            rez=ex3.execute(n,x);
        }catch(PolyglotException e)
        {
            System.out.println("eroare "+e.getMessage());
        }
    }
}
/*
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


 */