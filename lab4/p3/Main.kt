import java.io.File
//entitati
class Notita(
    private val titlu: String,
    private val continut: String,
    private val autor: Utilizator,
    private val data: String
)
{
    fun getTitlu()=titlu
    fun getContinut()=continut
    fun getData()=data
    fun getAutor()=autor
}
class Utilizator(
    private val nume:String,
)
{
    fun getNume()=nume
}
class Manager(private var notite:MutableList<Notita>)
{
    fun adaugaNotita(notita: Notita)
    {
        notite.add(notita)
    }
    fun afiseazaNotite()
    {
        for(notita in notite)
        {
            println(notita.getTitlu())
        }
    }
    fun stergeNotita(titlu: String): Boolean
    {
        val notita = notite.find { it.getTitlu() == titlu }
        if(notita != null)
        {
            notite.remove(notita)
            return true
        }
        return false
    }
}
//interfata
interface Meniu
{
    fun execute()
}
class SalvareDisk(private val notita:Notita):Meniu
{
    override fun execute()
    {
        val fisier=File("${notita.getTitlu()}.txt")
        fisier.writeText("${notita.getAutor().getNume()}-${notita.getContinut()}-${notita.getData()}")
    }
}
class Afisare(private var manager:Manager):Meniu
{
    override fun execute()
    {
        manager.afiseazaNotite()
    }
}
class Sterge(private var manager:Manager,private var titlu:String):Meniu
{
    override fun execute()
    {
        manager.stergeNotita(titlu)
    }
}
class Creare(private val manager: Manager, private val notita: Notita): Meniu
{
    override fun execute()
    {
        manager.adaugaNotita(notita)
    }
}
class StergereDisk(private val titlu: String): Meniu
{
    override fun execute()
    {
        val fisier=File("${titlu}.txt")
        if(fisier.exists())
        {
            fisier.delete()
        }
    }
}
class Aplicatie(private val manager: Manager)
{
    fun porneste()
    {
        while(true)
        {
            println("1. afisare notite")
            println("2. creare notita")
            println("3. stergere notita")
            println("0. pa")
            when(readLine()?.toIntOrNull())
            {
                1->Afisare(manager).execute()
                2->{
                    println("Titlu:")
                    val titlu = readlnOrNull() ?: ""//recomandare de la intellij in loc de readLine
                    println("Continut:")
                    val continut = readlnOrNull() ?: ""
                    println("Utilizator:")
                    val nume = readlnOrNull() ?: ""
                    val user=Utilizator(nume)
                    println("Data:")
                    val data= readlnOrNull() ?: ""
                    val notita= Notita(titlu,continut,user,data)
                    Creare(manager, notita).execute()
                    SalvareDisk(notita).execute()
                }
                3->{
                    println("Titlu:")
                    val titlu = readlnOrNull() ?: ""
                    Sterge(manager, titlu).execute()
                    StergereDisk(titlu).execute()
                }
                0->return
                else->throw IllegalArgumentException("alegere naspa")
            }
        }
    }
}
fun main()
{
    Aplicatie(Manager(mutableListOf())).porneste()
}