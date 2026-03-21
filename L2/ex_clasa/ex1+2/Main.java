import org.graalvm.polyglot.*;

public class Main
{

    private static int SumCRC(String token)
    {
        //pt ex 2
        if(token.length()>2)
        {
            token=token.substring(1,token.length()-1);
        }
        else
        {
            token="";//sir prea scurt
        }
        //
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
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

    static void main(String[] args) {
        Context polyglot = Context.create();
        Value array = polyglot.eval("js", "[\"If\",\"we\",\"run\"];");
        Value verif= polyglot.eval("python","ord('u')**2-1");
        for (int i = 0; i < array.getArraySize();i++){
            String element = array.getArrayElement(i).asString();
            int crc = SumCRC(element);
            System.out.println(element + " -> " + crc);
        }
        System.out.println(verif.asInt());//run - prima si ultima= u
        // inchidem contextul Polyglot
        polyglot.close();
    }
}
