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
    fun getTitlu()=titlu
    fun getDurata()=durata
    fun getGen()=gen
}

class Sala(private val nrSala:Int,
           private val nrLocuri:Int,
           private var nrLocuriDisponibile:Int)
{

}

class Bilet(private val pret:Double,
            private val film:Film,
            private val sala:Sala)
{
    fun get_pret()=pret
}

// sistem plata
interface ModPlata
{
    fun plateste(taxa:Double): Boolean
}

class PlataCash(private val client: Client) : ModPlata
{
    override fun plateste(taxa: Double): Boolean
    {
        if(taxa > 0.0 && taxa <= client.getCash())
        {
            client.scadeCash(taxa)
            return true
        }
        return false
    }
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

class PlataCard(private val contBancar:ContBancar): ModPlata
{
    override fun plateste(taxa: Double): Boolean
    {
        return contBancar.updateSume(taxa)
    }
}

// client
class Client(private val nume:String,
             private val varsta:Int,
             private val contBancar:ContBancar,
             private var cash:Double
)
{
    fun getNume()=nume
    fun getVarsta()=varsta
    fun getCash()=cash
    fun getBaniCard()=contBancar.getSumaDisponibila()
    fun scadeCash(valoare:Double)
    {
        if(valoare >= 0.0 )
        {
            cash -= valoare
        }
        else
        {
            cash += valoare
        }
    }
    fun cumparBilet(bilet:Bilet,modPlata:ModPlata): Boolean
    {
        return modPlata.plateste(bilet.get_pret())
        //pot adauga ulteroir plata voucher => nu modifc cod initial
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
    val cont = ContBancar(
        100.0,
        "123456789",
        Date.valueOf("2026-12-31"),
        123,
        "Ana"
    )


    val film = Film("Interstellar", 169, "SF")
    val sala = Sala(1, 100, 100)
    val bilet = Bilet(30.0,film,sala)
    val Ana=Client("Ana",19,cont,300.0)
    val cinema=Cinema(mutableListOf(),mutableListOf())//clienti,filme
    cinema.addFilm(film)
    cinema.addClient(Ana)

    val succ1=Ana.cumparBilet(bilet,PlataCard(cont))
    val succ2=Ana.cumparBilet(bilet,PlataCash(Ana))
    if(succ1)
    {
        println("Ana merge la film cu cardu ")
    }
    else
    {
        println("Ana sta acasa cu cardu")
    }
    if(succ2)
    {
        println("Ana merge la film cu cash ")
    }
    else
    {
        println("Ana sta acasa cu cash")
    }
    println("bani cash acm : ${Ana.getCash()}")
    println("bani card acm:${Ana.getBaniCard()}")
}