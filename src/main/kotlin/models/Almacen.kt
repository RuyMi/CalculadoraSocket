package models

import java.io.Serializable

class Almacen: Serializable {
     private val listaOperaciones: ArrayList<Operacion> = ArrayList<Operacion>()
     private val maxCache = 3


     @Synchronized
     fun add(r: Operacion) {
         if(listaOperaciones.size == 3){
             listaOperaciones.removeAt(0)
             listaOperaciones.add(r)
             
         }else{
             listaOperaciones.add(r)
         }
     }

     @Synchronized
     fun show() {
         listaOperaciones.forEach {
             println(it)
         }

     }


     @Synchronized
     fun size(): Int {
         return listaOperaciones.size

     }
}