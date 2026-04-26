/*
adt=algebric data type
 */
package org.example
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

data class RssFeed(
    val title:String,
    val link:String,
    val description:String,
    val pubDate: String,
    var items:List<Item>
)
{
    companion object//echivalent static in java
    {
        //privvate pt ca apelez doar din interou clasei
        fun fromRssFeed(exempluRssFeed: String): RssFeed {
            val doc = Jsoup.parse(exempluRssFeed, "", Parser.xmlParser())
            val title = doc.selectFirst("title")?.text() ?: ""
            val link = doc.selectFirst("link")?.text() ?: ""
            val description = doc.selectFirst("description")?.text() ?: ""
            val pubDate = doc.selectFirst("pubDate")?.text() ?: ""
            val items = doc.select("item").map { item ->
                Item(
                    item.selectFirst("title")?.text() ?: "",
                    link = item.selectFirst("link")?.text() ?: "",
                    description = item.selectFirst("description")?.text() ?: "",
                    pubDate = item.selectFirst("pubDate")?.text() ?: ""
                )
            }
            return RssFeed(title, link, description, pubDate, items)
        }
    }
    fun show()
    {
        for(item in items)
        {
            println("${item.title}-${item.link}")
        }
    }
}
data class Item(
    val title:String,
    val link:String,
    val description:String,
    val pubDate: String
)
{}
fun main()
{
    val exempluRssFeed="""
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
    RssFeed.fromRssFeed(exempluRssFeed).show()
}