**ACTIVIDAD DE APRENDIZAJE DE PROGRAMACION MULTIMEDIA Y DISPOSITIVOS MOVILES - 1ª Evaluación**

Se ha realizado una app android conectada a una base de datos **ROOM DATABASE**, en la cual 
dispongo de tres clases, relacionadas con una tienda online a la que he llamado **Retroshop**

Estas clases son:
* **Client**: Representa al cliente/s que se ha registrado en la app
* **Product**: Representa al producto/s disponible, o no, en la tienda online
* **Repair**: Representa al pedido de reparacion/es

Cada clase contiene sus atributos con tipos de datos diferentes (String, int, double, boolean, Date
y byte[] para almacenar la imagen).

De cada clase se ha hecho un **CRUD**, incluyendo campos y elementos **TextView**, **EditText**, 
**Button**, **CheckBox**, **ImageView**, **RecyclerView**, **DatePicker**, **Toast** 
(para los mensajes de confirmación de registro, actualizacion y borrado, ademas de avisos 
de rellenar campos requeridos, etc...). También se han utilizado **Adapters** para cada clase.

Tanto en Client como en Product, se puede interactuar y acceder a la *Galeria* del dispositivo 
virtual para seleccionar una foto de la clase correspondiente

También se ha añadido **MapBox** para el caso de localizar los clientes registrados. Se ha
configurado un **ActionBar** para acceder al mapa en pantalla, ademas de incluir **Idiomas**
como el **Español** e **Inglés**

También se ha seguido una pauta importante durante la realización del proyecto a traves de
**GitHub**, utilizando comandos **Git** en la terminal, creando ramas especificas. Finalmente
mergeadas a la rama principal master. A continuación incluyo dicho enlace:
https://github.com/Joserra2304/AA_PMDM_EV1.git

Finalmente, en el apartado de Github de **Issues** he añadido algun problema que sucede en la APP, 
como por ejemplo, no aparece en el dispositivo movil (físico) no abre la lista de clientes, 
y sucede lo mismo con productos, si manipulas el registro añadiendo alguna imagen (quiza por exceso de tamaño),
la lista de reparaciones se abre correctamente.
