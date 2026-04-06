/*
Utilizând biblioteca JSoup, să se proceseze un RSS feed (de exemplu:
http://rss.cnn.com/rss/edition.rss) construind un ADT pentru stocarea datelor. ADT-ul trebuie
să conțină o listă de elemente (item-uri; un item poate fi un ADT separat), precum și
atributele generale (title, link, description, pubDate). La final, se va afișa titlul și link-ul
fiecărui item.
 */
package org.example
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

data class RSS_FEED(
    val title:String,
    val link:String,
    val description:String,
    val items:List<RSS_ITEM>
)
{
    data class RSS_ITEM(
        val title:String,
        val link:String,
        val description:String,
        val pubDate:String
    )
}
//ADT= algebric data Type
fun main()
{
    val rssString = """
    <?xml version="1.0" encoding="UTF-8" ?>
    <rss version="2.0">
    <channel>
        <title>PP - Laborator 3</title>
        <link>http://mike.tuiasi.ro/LabPP3.pdf</link>
        <description>Aplicatii simple ale ADT in Kotlin</description>
        <item>
            <title>Crearea unui proiect Maven</title>
            <link>http://mike.tuiasi.ro/LabPP3.pdf</link>
            <description>Adaugarea dependentelor in pom.xml</description>
        </item>
        <item>
            <title>Expresii regulate</title>
            <link>http://mike.tuiasi.ro/LabPP3.pdf</link>
            <description>Exemple de expresii regulate in Kotlin</description>
        </item>
    </channel>
    </rss>
""".trimIndent()
    //citire RSS

    //val url = "http://rss.cnn.com/rss/edition.rss" pt rss online
    //val doc= Jsoup.parse(rssString) fail cu asta la link
    val doc = Jsoup.parse(rssString, "", Parser.xmlParser())
    // fara asta jsoup vede <link> si crede ca e un tag html gol
    //extragere date din <chanel>
    val channel=doc.selectFirst("channel")
    val feedTitle= doc.selectFirst("title")?.text() ?: ""
    val feedLink= doc.selectFirst("link")?.text() ?: ""
    val feedDescription=doc.selectFirst("description")?.text() ?: ""

    //extragere info item uri
    val items = doc.select("item").map { item -> RSS_FEED.RSS_ITEM(
            title = item.selectFirst("title")?.text() ?: "",
            link = item.selectFirst("link")?.text() ?: "",
            description = item.selectFirst("description")?.text() ?: "",
            pubDate = item.selectFirst("pubDate")?.text() ?: ""
        )
    }
    //construiesc adt pt feed
    val rez= RSS_FEED(feedTitle,feedLink,feedDescription,items)
    //afisez ce vrea bossu
    for(item in items)
        println("${item.link} , ${item.title}")
}
// diferenta dintr doc.select si doc. selectFirst
// primul ia toate , al doilea doar primul
// daca folosesc la channel select, imi ia titlurile inclusiv de la item uri=> trb selectFirst

//?: "" = Elvis Operator (default value)

//val feedTitle = doc.selectFirst("title")?.text() ?: ""
//Dacă expresia din stânga (doc.selectFirst("title")?.text()) e null → se folosește valoarea din dreapta "" (string gol).
//Dacă nu e null → folosește valoarea returnată.