(function()
        {

            var ceroIzquierdaTiempo = function(number)
            {
                if(number<10)
                    return '0'+number;
                
                return number;
            }

            var actualizarHora = function()
            {    
                var semana = ["Domingo", "Lunes", "Martes", "Miercoles","Jueves","Viernes","Sabado"],
                meses = ["Enero", "Febrero", "Marzo", 'Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre',
                        'Noviembre','Diciembre'];
                var fecha = new Date();
               
                // 

                //Fecha
                document.getElementById("diaSemana").textContent = semana[fecha.getDay()];
                document.getElementById("dia").textContent = fecha.getDate();
                document.getElementById("mes").textContent = meses[fecha.getMonth()];
                document.getElementById("year").textContent = fecha.getFullYear();

                //time
                var horas = fecha.getHours();
                var minutos = fecha.getMinutes();
                var segundos = fecha.getSeconds();
                
                if(horas>12)
                {
                    horas = horas - 12;
                    document.getElementById("ampm").textContent = "PM"
                }
                else
                {
                    
                    if(horas==0)
                    {
                        horas = 12; 
                        document.getElementById("ampm").textContent = "PM"
                    }
                    else
                    {
                        document.getElementById("ampm").textContent = "AM"
                    }
                        
                }
                document.getElementById("horas").textContent = horas; 
                document.getElementById("minutos").textContent = ceroIzquierdaTiempo(fecha.getMinutes());
                document.getElementById("segundos").textContent = ceroIzquierdaTiempo(fecha.getSeconds());
                
            };   
            setInterval(actualizarHora,1000);
            
        }())