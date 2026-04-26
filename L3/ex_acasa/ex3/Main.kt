package org.example
import org.jsoup.Jsoup

val visited=mutableListOf<String>()
class Node(val url:String)
{
    val children= mutableListOf<Node>()
}
fun crawl(node:Node,depth:Int,domain:String)
{
    if(depth>2) // max recursie ordin2
    {
        return
    }
    if(node.url in visited)//l am vizitat deja
    {
        return
    }
    try
    {
        visited.add(node.url)
        val doc= Jsoup.connect(node.url).get()
        //la Jsoup.parse(url) , trb sa ai url ca string , de exemplu dupa khttp.get()
        val links=doc.select("a[href]")//select link uri cu atribut href
        //ca sa ignore <a name=.......</a>
        for(link in links)
        {
            val href=link.absUrl("href")//link absolut fara /ceva la final
            if(href.contains(domain))//nu ma duc pe alte site uri, raman in acelasi domeniu
            {
                val child=Node(href)
                node.children.add(child)
                crawl(child,depth+1,domain)
            }
        }
    }catch (e: Exception)
    {
        println("eroare ${e} la  ${node.url}")
        return
    }
}
fun serializeTree(node:Node,depth:Int)
{
    if(depth>10)
    {
        return
    }
    println(" ".repeat(depth)+node.url)
    for(child in node.children)
    {
        serializeTree(child,depth+1)
    }
}
fun deserializeTree(string:String):Node
{
    val lines=string.lines().filter { it.isNotBlank() }
    val root=Node(lines[0])
    val nodes=mutableListOf<Node>(root)//lista nodurilor create in ordinea aparitiei
    for(i in 1 until lines.size)//parcurgere linii
    {
        val line=lines[i]
        val spaces=line.length - line.trimStart().length
        val node=Node(line.trimStart())
        //caut parintele in sus
        var j=i-1
        while(j>=0)
        {
            val parentSpaces=lines[j].length - lines[j].trimStart().length
            if(spaces == parentSpaces+1)
            {
                nodes[j].children.add(node)
                break
            }
            j--
        }
        nodes.add(node)
    }
    return root
}
fun main()
{
    /*
    val rootURL= "http://mike.tuiasi.ro/"
    val rootDOMAIN="mike.tuiasi.ro"
    val root=Node(rootURL)
    crawl(root,0,rootDOMAIN)
    serializeTree(root,0)

     */
    val treeStr = """
    http://mike.tuiasi.ro/
     http://mike.tuiasi.ro/page1
      http://mike.tuiasi.ro/page1/sub1
     http://mike.tuiasi.ro/page2
    """.trimIndent()
    val root1 = deserializeTree(treeStr)
    serializeTree(root1,0)
}
