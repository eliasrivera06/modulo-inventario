document.addEventListener('DOMContentLoaded', function() {
    const inventarioForm = document.getElementById('inventarioForm');
    
    // Manejar el envío del formulario
    inventarioForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        // Mostrar indicador de carga
        const submitButton = inventarioForm.querySelector('button[type="submit"]');
        const originalButtonText = submitButton.innerHTML;
        submitButton.disabled = true;
        submitButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Enviando...';
        
        try {
            // Obtener los valores del formulario
            const formData = {
                nombreSolicitante: document.getElementById('nombre').value.trim(),
                codigoSolicitante: document.getElementById('codigo').value.trim(),
                sedeOrigen: document.getElementById('sede').value,
                fecha: document.getElementById('fecha').value,
                codigoProducto: document.getElementById('codigoProducto').value.trim(),
                nombreProducto: document.getElementById('nombreProducto').value.trim(),
                categoria: document.getElementById('categoria').value,
                cantidad: parseInt(document.getElementById('cantidad').value, 10),
                comentario: document.getElementById('comentario').value.trim(),
                almacenDestino: document.getElementById('almacenDestino').value,
                tipoAccion: document.querySelector('input[name="tipoAccion"]:checked').value
            };
            
            // Uso de Fetch API para llamadas HTTP
            const response = await fetch('http://localhost:8080/api/inventario', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData)
            });
            
            const result = await response.json();
            
            if (response.ok) {
                // Mostrar mensaje de éxito
                await Swal.fire({
                    icon: 'success',
                    title: '¡Éxito!',
                    text: 'La solicitud de inventario se ha registrado correctamente.\nSe descargará un archivo con los detalles.',
                    confirmButtonText: 'Aceptar'
                });

                // Limpiar el formulario
                inventarioForm.reset();
                
                // Descargar el archivo de texto
                const blob = new Blob([result.ticket], { type: 'text/plain' });
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = result.nombreArchivo || 'solicitud_inventario.txt';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
            } else {
                // Mostrar mensaje de error
                throw new Error(result.message || 'Error al procesar la solicitud');
            }
            
        } catch (error) {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al procesar la solicitud: ' + error.message,
                confirmButtonText: 'Aceptar'
            });
        } finally {
            // Restaurar el botón
            submitButton.disabled = false;
            submitButton.innerHTML = originalButtonText;
        }
    });
    
    // Función para simular la búsqueda de producto (puedes personalizar según tus necesidades)
    document.getElementById('buscarProducto').addEventListener('click', function() {
        const codigo = document.getElementById('codigoProducto').value.trim();
        if (codigo) {
            // Aquí podrías hacer una llamada a tu API para buscar el producto
            // Por ahora, simulamos una respuesta
            setTimeout(() => {
                document.getElementById('nombreProducto').value = `Producto de prueba ${codigo}`;
            }, 500);
        }
    });
    
    // Configurar la fecha actual por defecto
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('fecha').value = today;
});
