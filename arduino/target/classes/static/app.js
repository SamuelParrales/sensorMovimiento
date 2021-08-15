var ws;
const alarma = new Audio("/alarm.mp3");
function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
}
//i
function connect() {
	ws = new WebSocket('ws://192.168.1.64:8080/user');
	//ws = new WebSocket('ws://localhost:8080/user');
	ws.onmessage = function(data) //Cada vez que el objeto recibe un mensaje
	{
		playAlarma(data.data);
	}
	setConnected(true);
}

function disconnect() {
	if (ws != null) {
		ws.close();
	}
	setConnected(false);
	console.log("Websocket is in disconnected state");
}


function playAlarma(message) {
	
	let lista = document.getElementById("movimientos-detectados");
	let cantElementos = lista.children.length
	
	let nuevaTarea = document.createElement("li"),
        enlace = document.createElement("a"),
        contenido = document.createTextNode('1. '+message); //Crea el contenido del nodo
        enlace.appendChild(contenido);  //Se inserta el contenido de la tarea
        //Se le establece el atributo href
        enlace.setAttribute("href","#");
        //Se añade el enlace (a) al elment (i)
        nuevaTarea.append(enlace);
		if(cantElementos===0)
			lista.appendChild(nuevaTarea)
		else
        	lista.insertBefore(nuevaTarea,lista.children[0]); //Se añade la nueva tarea a la lista
		
		cantElementos = lista.children.length;
		if(cantElementos===6)
		{
			let nodoEliminar = lista.children[cantElementos-1]; 
			lista.removeChild(nodoEliminar)
			cantElementos=cantElementos-1;
		}
		
		for (let i=1;i<cantElementos;i++)
		{
			let hijo = lista.children[i].children[0];
			
			let contenido = i+1 + ". " + hijo.innerHTML.substring(2);
			hijo.innerHTML = contenido;
		}
		//Reproudcir alarma 

        alarma.play();
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
});