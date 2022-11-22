package client

import models.Almacen
import models.Operacion
import models.TipoOperacion
import java.io.DataInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket
import java.util.*


fun main(){
    val direccion: InetAddress = InetAddress.getLocalHost()
    val servidor: Socket
    val puerto = 7531


    try{
        servidor = Socket(direccion, puerto)
        println("Conectando al servidor...")
        //Primero leemos el caché que del servidor
        val listaEntrante = ObjectInputStream(servidor.getInputStream())
        val listaOperacion: Almacen = listaEntrante.readObject() as Almacen
        if(listaOperacion.size() == 0){
            println("No hay operaciones en caché")
        }else{
            println(listaOperacion.show())
        }

        //Despues enviamos la operación
        val mandar = enviarDatos()
        val escribir = ObjectOutputStream(servidor.getOutputStream())
        escribir.writeObject(mandar)
        val resultado = DataInputStream(servidor.getInputStream()).readDouble()
        println(resultado)
        servidor.close()



    }catch (e: Exception){
        e.printStackTrace()
    }




}

private fun enviarDatos(): Operacion {
    println("Bienvenido a la calculadoraSocket, digame los siguientes datos para continuar")
    println("Digame el primer numero:")
    val num1 = readln().toInt()
    println("Digame el segundo numero:")
    val num2 = readln().toInt()
    println("Digame el tipo de operacion a realizar (Sumar, Restar, Multiplicar, Dividir)")
    val operacion = TipoOperacion.valueOf(readln().uppercase(Locale.getDefault()))
    return Operacion(num1, num2, operacion)

}