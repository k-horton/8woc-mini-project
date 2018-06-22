/*
fun main(args: Array<String>) {
    val catalog = RetrofitClient().getCatalogAPI()

    val call = catalog.getBibles()



    val root = call.execute().body()
    val d43Data: List<Language>

    if (root != null) {
        d43Data = D43toBibles(root)

        val urlTest = getBook(d43Data[7].versions[0].books[0].url)

        println(urlTest.name)
        for (cht in urlTest.chapters) {
            println("chapter: " + cht.num)
            println(cht.verses)
        }



    }
}

*/