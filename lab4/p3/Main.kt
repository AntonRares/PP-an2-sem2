import java.io.File
import java.sql.Date
//entitati
class Notita(
    private val titlu: String,
    private val continut: String,
    private val autor: Utilizator,
    private val data: Date
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
class StergereDisk(private val notita:Notita): Meniu
{
    override fun execute()
    {
        val fisier=File("${notita.getTitlu()}.txt")
        if(fisier.exists())
        {
            fisier.delete()
        }
    }
}
fun main()
{
    val user1 = Utilizator("Ana")
    val user2= Utilizator("Rares")
    val manager = Manager(mutableListOf())

    val notita1 = Notita(
        "tema",
        "de facut laborator",
        user1,
        Date.valueOf("2025-05-20")
    )
    val notita2 = Notita(
        "cumparaturi",
        "steroizi",
        user2,
        Date.valueOf("2025-05-20")
    )
    Creare(manager, notita1).execute()
    SalvareDisk(notita1).execute()

    Creare(manager, notita2).execute()
    SalvareDisk(notita2).execute()


    println("Lista notitelor:")
    Afisare(manager).execute()

    Sterge(manager, "tema").execute()

    StergereDisk(notita1).execute()

    println("Lista dupa stergere:")
    Afisare(manager).execute()
}