package np.com.bimalkafle.myapplication.controllers

import com.google.firebase.database.FirebaseDatabase
import np.com.bimalkafle.myapplication.model.Order


object OrderRepository{
    private val db = FirebaseDatabase.getInstance().getReference("orders")

    fun getAllOrder(onResult: (List<Order>) -> Unit) {
        db.get().addOnSuccessListener { snapshot ->
            val orderList = mutableListOf<Order>()

            for (orderSnapshot in snapshot.children) {
                val order = orderSnapshot.getValue(Order::class.java)
                order?.let { orderList.add(it) }
            }

            onResult(orderList)
        }.addOnFailureListener {
            onResult(emptyList()) // or handle error more explicitly
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