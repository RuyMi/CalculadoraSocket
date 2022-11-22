package server

import models.Almacen
import java.net.ServerSocket

fun main(){
    var numeroClientes = 0
    val puertoServidor = 7531
    val almacen = Almacen()
    var servidor: ServerSocket

    println("Iniciando servidor...")
    servidor = ServerSocket(puertoServidor)
    try{
        while(numeroClientes < 10){
            println("Esperando conexion...")
            val cliente = servidor.accept()
            numeroClientes++
            // Pasamos el control al hilo correspondiente
            println("Petición --> ${cliente.inetAddress} se ha conectado con el pruerto: ${cliente.port}")
            val gc = GestionClientes(numeroClientes, cliente, almacen)
            gc.start()
        }
        println("Servidor finalizado...")
        servidor.close()
    }catch (e: Exception) {
        println("Error iniciando el servidor: ${e.printStackTrace()}")
    }finally {
        servidor.close()
    }


}