package np.com.bimalkafle.myapplication.controllers

import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.myapplication.model.Items
import np.com.bimalkafle.myapplication.model.Order


object OrderRepository{
    private val db = FirebaseDatabase.getInstance().getReference("orders")

    fun getAllOrder(onResult: (List<Order>) -> Unit) {
        db.get().addOnSuccessListener { snapshot ->
            val orderList = mutableListOf<Order>()

            for (orderSnapshot in snapshot.children) {
                try {
                    val orderId = orderSnapshot.child("orderId").getValue(String::class.java) ?: ""
                    val userId = orderSnapshot.child("userId").getValue(String::class.java) ?: ""
                    val totalPrice = orderSnapshot.child("totalPrice").getValue(String::class.java) ?: ""
                    val createAt = orderSnapshot.child("createAt").getValue(String::class.java) ?: ""

                    // Safe item list extraction
                    val itemSnapshot = orderSnapshot.child("items")
                    val itemsList = mutableListOf<Items>()
                    for (itemChild in itemSnapshot.children) {
                        val item = itemChild.getValue(Items::class.java)
                        if (item != null) itemsList.add(item)
                    }

                    val order = Order(
                        orderId = orderId,
                        userId = userId,
                        totalPrice = totalPrice,
                        createAt = createAt,
                        item = itemsList
                    )

                    orderList.add(order)
                } catch (e: Exception) {
                    println("Error parsing order: ${e.message}")
                }
            }

            onResult(orderList)
        }.addOnFailureListener {
            println("Firebase error: ${it.message}")
            onResult(emptyList())
        }
    }


    fun deleteOrder(order: Order){
        val orderId = order.orderId

        if (orderId.isBlank()) {
            return
        }
        db.child(orderId).removeValue()
    }
}