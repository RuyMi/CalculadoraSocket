package server

import models.Almacen
import models.Operacion
import models.TipoOperacion
import java.io.*
import java.net.Socket

class GestionClientes(
    private var numCliente: Int = 0,
    private val cliente: Socket,
    private val almacen: Almacen

): Thread() {

    override fun run() {
        val bufferObjetosEntrada : ObjectInputStream
        val bufferObjetosSalida =   ObjectOutputStream(cliente.getOutputStream())
        try{
            //Primero mandamos las operaciones que tenemos en caché.
            println("Hilo de atención al Cliente: $numCliente de ${cliente.inetAddress}")
            mandarCache(bufferObjetosSalida)
            //Despues esperamos a que nos mande la operacion a realizar
            bufferObjetosEntrada = ObjectInputStream(cliente.getInputStream())
            val operacion: Operacion = bufferObjetosEntrada.readObject() as Operacion
            realizarOperacion(operacion)
            println(operacion)
            val buffer2 = DataOutputStream(cliente.getOutputStream())
            operacion.resultado?.let { buffer2.writeDouble(it) }

            cliente.close()
            bufferObjetosSalida.close()


        }catch (e: IOException){
            e.printStackTrace()

        }
    }

    private fun realizarOperacion(operacion: Operacion) {
        when(operacion.operacion){
            TipoOperacion.SUMAR -> operacion.resultado = (operacion.numero1 + operacion.numero2).toDouble()
            TipoOperacion.RESTAR -> {
                if(operacion.numero2 != 0){
                    operacion.resultado = (operacion.numero1 - operacion.numero2).toDouble()
                }else{
                    operacion.resultado = Double.NaN
                }
            }
            TipoOperacion.MULTIPLICAR -> operacion.resultado = (operacion.numero1 * operacion.numero2).toDouble()
            TipoOperacion.DIVIDIR -> operacion.resultado = (operacion.numero1 / operacion.numero2).toDouble()
        }
        almacen.add(operacion)
        
    }

    private fun mandarCache(bufferObjetosSalida: ObjectOutputStream) {
        bufferObjetosSalida.writeObject(almacen)
    }




}