import java.sql.Date
/*
SOLID
1-fiecare clasa are o singura responsabilit,
2-open/closed , ex interfata mod plata, am adaugat functionalit noua fara sa modific cod
3-Liskov onj deriv pot inloc obj de baza
4-interfetele trb mici si specifice
5- clasele de nivel inalt nu depind de implem concrete


 */
// entitati
class Film(private val titlu:String,
           private val durata:Int,
           private val gen:String)
{
    fun getDurata()=durata
    fun getGen()=gen
}

class Sala(private val nrSala:Int,
           private val nrLocuri:Int,
           private var nrLocuriDisponibile:Int)
{
}

class Bilet(private val pret:Double,
            private val data:String,
            private val film:Film,
            private val sala:Sala)
{
    fun getPret()=pret
}

// sistem plata
interface ModPlata
{
    fun plateste(taxa:Double): Boolean
}

class PlataCash(private var baniCash:BaniCash) : ModPlata
{
    override fun plateste(taxa: Double): Boolean
    {
        return baniCash.updateSuma(taxa)
    }
}
class PlataCard(private val contBancar:ContBancar): ModPlata
{
    override fun plateste(taxa: Double): Boolean
    {
        return contBancar.updateSume(taxa)
    }
}
//bani cash si contu bancar
class BaniCash(private var sumaDisponibila:Double)
{
    fun updateSuma(valoare:Double):Boolean
    {
        if(valoare>0.0 && sumaDisponibila >= valoare)
        {
            sumaDisponibila-=valoare
            return true
        }
        return false
    }
    fun getSumaDisponibila()=sumaDisponibila
}
class ContBancar(private var sumaDisponibila: Double,
                 private val nrCard:String,
                 private val dataExpirare: Date,
                 private val cvvCode:Int,
                 private val userName:String)
{
    fun getSumaDisponibila()=sumaDisponibila
    fun updateSume(valoare:Double): Boolean
    {
        if(valoare > 0 && valoare <= sumaDisponibila)
        {
            sumaDisponibila -= valoare
            return true
        }
        return false
    }
}

// client
class Client(private val nume:String,
             private val varsta:Int,
             private val contBancar:ContBancar,
             private val baniCash:BaniCash
)
{
    fun getNume()=nume
    fun getVarsta()=varsta
    fun getBaniCard()=contBancar.getSumaDisponibila()
    fun getBaniCash()=baniCash.getSumaDisponibila()

}
class ServiciuCumparare(private val client:Client)//pt srp
{
    fun cumparBilet(bilet:Bilet,modPlata:ModPlata): Boolean
    {
        return modPlata.plateste(bilet.getPret())
    }
}
// cinema
class Cinema(private var clienti:MutableList<Client>,
             private var filme:MutableList<Film>)
{
    fun addClient(client:Client):Boolean
    {
        if(client.getNume().isNotEmpty() && client.getVarsta() > 15)
        {
            clienti.add(client)
            return true
        }
        return false
    }

    fun addFilm(f:Film):Boolean
    {
        if(f.getDurata() > 60 && f.getGen().isNotEmpty())
        {
            filme.add(f)
            return true
        }
        return false
    }
}
// main
fun main()
{
    val contBancar = ContBancar(
        100.0,
        "123456789",
        Date.valueOf("2026-12-31"),
        123,
        "Ana"
    )

    val baniCash = BaniCash(50.0)

    val film1 = Film("Interstellar", 169, "SF")
    val film2= Film("War Machine",112,"jmecher")
    val sala1 = Sala(1, 100, 100)
    val sala2= Sala(2,50,30)
    val bilet1 = Bilet(30.0,"31.9.2025",film1,sala1)
    val bilet2= Bilet(20.0,"20.11.2025",film2,sala2)
    val Ana=Client("Ana",19,contBancar,baniCash)
    val cinema=Cinema(mutableListOf(),mutableListOf())//clienti,filme
    cinema.addFilm(film1)
    cinema.addFilm(film2)
    cinema.addClient(Ana)
    val serviciu=ServiciuCumparare(Ana)
    if(serviciu.cumparBilet(bilet1,PlataCard(contBancar)))
    {
        println("merge Ana la film1")
    }
    else
    {
        println("Ana sta acasax1")
    }
    if(serviciu.cumparBilet(bilet2,PlataCash(baniCash)))
    {
        println("merge Ana la film2")
    }
    else
    {
        println("Ana sta acasax2")
    }
    println("bani card:${Ana.getBaniCard()} ; bani cash:${Ana.getBaniCash()}")
}