package org.example;
import org.graalvm.polyglot.*;
public class Main
{

    private static int SumCRC(String token)
    {
        Context polyglot = Context.newBuilder().option("engine.WarnInterpreterOnly","false").allowAllAccess(true).build();
        //fara warning uri!!!!
        polyglot.getBindings("python").putMember("token",token);
        //X^2-1 polinom ales
        Value result = polyglot.eval("python", """
                def crc(token):
                    s=0
                    for ch in token:
                        s+=ord(ch)**2-1
                    return s
                crc(token)
                """);

        int resultInt = result.asInt();
        // inchidem contextul Polyglot
        polyglot.close();

        return resultInt;
    }

    static void main(String[] args)
    {
        //ex 1 tema
        Context polyglot = Context.newBuilder().option("engine.WarnInterpreterOnly","false").allowAllAccess(true).build();
        // fara warning uri!!!!!, at try cu try dar imi e lene sa scriu
        Value array = polyglot.eval("js", "[\"If\",\"we\",\"run\",\"we\",\"slabim\",\"faster\"];");

        int size=(int) array.getArraySize();
        String[] words=new String[size];
        int[] sume=new int[size];

        for (int i = 0; i < size;i++){
            String element = array.getArrayElement(i).asString();
            int crc = SumCRC(element);
            System.out.println(element + " -> " + crc);
            //
            words[i]=element;
            sume[i]=crc;
        }
        System.out.println("perechi de cuv cu aceeasi suma de control:");
        for(int i=0;i<size;i++)
        {
            for(int j=i+1;j<size;j++)
            {
                if(sume[i]==sume[j])
                {
                    System.out.println(words[i]+" "+words[j]);
                }
            }
        }
        polyglot.close();
    }
}
/*output:
If -> 15731
we -> 24360
run -> 38782
we -> 24360
slabim -> 66802
faster -> 69685
perechi de cuv cu aceeasi suma de control:
we we
 */