package org.example;
import org.graalvm.polyglot.*;
public class Main
{

    static void main(String[] args)
    {

        try(Context polyglot = Context.newBuilder().option("engine.WarnInterpreterOnly","false").allowAllAccess(true).build())
        {

            polyglot.close();
        }
    }
}