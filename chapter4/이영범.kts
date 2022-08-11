
class Cart(val name: String, val price: Long)
class Button(val id: String, val cart: Cart)

var shoppingCart = mutableListOf<Cart>()
var shoppingCartTotal = 0L

val wishList = arrayOf(
    Cart("상의", 20000),
    Cart("하의", 20000),
    Cart("하의", 20000),
    Cart("하의", 20000),
    Cart("하의", 20000),
    Cart("모자", 20000)
)

// DOM
val buttons = arrayOf(
    Button("_id00", Cart("상의", 20000)),
    Button("_id01", Cart("하의", 20000)),
    Button("_id02", Cart("모자", 10000))
)

fun getBuyButtonsDom(): Array<Button> = buttons
fun setTaxDom(amount: Double): Unit = println("set tax dom to $amount")
fun setCartTotalDom(): Unit = println("set cart total dom to $shoppingCartTotal")
fun showFreeShoppingIcon(id: String): Unit = println("Free $id !!")
fun showHideShoppingIcon(id: String): Unit = println("Hide $id !!")

fun addItemToCart(name: String, price: Long) {
    shoppingCart = addItem(shoppingCart, name, price).toMutableList()
    calcCartTotal()
}

fun calcCartTotal() {
    shoppingCartTotal = calcTotal(shoppingCart)
    setCartTotalDom()
    updateShoppingIcons()
    updateTaxDom()
}

fun updateShoppingIcons() {
    val buttons = getBuyButtonsDom()
    for (i in buttons.indices) {
        val itemId = buttons[i].id
        val itemPrice = buttons[i].cart.price
        if (getsFreeShopping(shoppingCartTotal, itemPrice)) {
            showFreeShoppingIcon(itemId)
        } else {
            showHideShoppingIcon(itemId)
        }
    }
}

fun updateTaxDom(): Unit = calcTax(shoppingCartTotal).let { setTaxDom(it) }

fun addItem(shoppingCart: List<Cart>, name: String, price: Long): List<Cart> {
    val newShoppingCart = shoppingCart.toMutableList()
    newShoppingCart.add(Cart(name, price))
    return newShoppingCart
}

fun calcTotal(shoppingCart: List<Cart>): Long = (shoppingCart.indices)
    .asSequence()
    .map { shoppingCart[it].price }
    .sum().toLong()

fun getsFreeShopping(total: Long, itemPrice: Long): Boolean = itemPrice + total >= 60000

fun calcTax(amount: Long): Double = amount * 0.10

wishList.forEach {
    addItemToCart(it.name, it.price)
}