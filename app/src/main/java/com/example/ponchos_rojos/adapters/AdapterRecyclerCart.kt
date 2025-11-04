package com.example.ponchos_rojos.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.DataClass.GameInfo
import com.example.ponchos_rojos.databinding.AdapterRecyclerCartBinding
import java.util.Locale

class AdapterRecyclerCart(private val context: Context,
                          private val cartList: MutableList<GameInfo>,
                          private val yourCartIsEmpty: TextView,// mutable para poder eliminar
                          private val pricesUpdate: TextView,
                          private val payButton: Button
) : RecyclerView.Adapter<AdapterRecyclerCart.CardCartViewHolder>() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesButton: SharedPreferences



    // ViewHolder
    inner class CardCartViewHolder(private val binding: AdapterRecyclerCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GameInfo) {
            sharedPreferences = binding.root.context.getSharedPreferences("JuegosCarrito", Context.MODE_PRIVATE)
            sharedPreferencesButton = binding.root.context.getSharedPreferences("logicButton", Context.MODE_PRIVATE)

//if(sharedPreferences.all.isEmpty()){
//    cartList.clear()
//}

                binding.titleGameCartSection.text = item.name

                binding.priceGameCartSection.text = "$" + item.price

                val imageId = binding.root.context.resources.getIdentifier(
                    item.imageName, "drawable", binding.root.context.packageName
                )
                if (imageId != 0) {
                    binding.imageGameCart.setImageResource(imageId)
                }


//            if(cartList.isEmpty()){
//                yourCartIsEmpty.visibility = View.VISIBLE
//
//                payButton.visibility = View.GONE
//
//            }else{
//                yourCartIsEmpty.visibility = View.GONE
//
//                payButton.visibility = View.VISIBLE
//            }



            binding.removeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    cartList.removeAt(position)

                    notifyItemRemoved(position)

                    //val totalPrice: Double = sumaPrecios(cartList)
                   // val totalString: String = totalPrice.toString()
                    val totalString:String = sumaPrecios(cartList)
                    pricesUpdate.text = "$$totalString"

                    //SHARED REFERENCES
                    val editor = sharedPreferences.edit()
                    val editorButton = sharedPreferencesButton.edit()
                    editor.remove("idGame_${item.name}")
                    editorButton.putBoolean("boton_presionado_${item.name}", false)
                    editor.apply()
                    editorButton.apply()
                }
                if(cartList.isEmpty()){
                    yourCartIsEmpty.visibility = View.VISIBLE

                    payButton.visibility = View.GONE
                }else{
                    yourCartIsEmpty.visibility = View.GONE

                    payButton.visibility = View.VISIBLE
                }

            }

//            binding.imageGameCart.setOnClickListener {
//                val intent = Intent(context, activity_gameInfo::class.java)
//                intent.putExtra("gameData", item) // enviamos el objeto completo
//                context.startActivity(intent)
//            }
        }

        fun sumaPrecios(cartList: MutableList<GameInfo>):String{
            var suma:Double = 0.0
            //var decimal:Double = 0.0
            if(!cartList.isEmpty()){
                for(i in 0 until  cartList.size){
                    suma += cartList[i].price.toDouble()
                }
               // decimal = String.format(Locale.US,"%.2f", suma).toDouble()
                return String.format(Locale.US,"%.2f", suma)
            }else{
                //pricesUpdate.text = "$"+decimal.toString()
                return suma.toString()
            }

            //return decimal

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCartViewHolder {
        //context = parent.context
        return CardCartViewHolder(
            AdapterRecyclerCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CardCartViewHolder, position: Int) {
       holder.bind(cartList[position])

    }

    override fun getItemCount(): Int = cartList.size
}