        // Contador de seleccionados
        function updateSelectedCount() {
            const checkboxes = document.querySelectorAll('.producto-checkbox:checked');
            const countElement = document.getElementById('selected-count');
            countElement.textContent = checkboxes.length + ' seleccionados';
            countElement.style.display = checkboxes.length > 0 ? 'inline-block' : 'none';
        }
        
        // Seleccionar/deseleccionar todos
        function toggleSelectAll(source) {
            const checkboxes = document.querySelectorAll('.producto-checkbox');
            checkboxes.forEach(checkbox => {
                checkbox.checked = source.checked;
            });
            updateSelectedCount();
        }
        
        // Funciones para acciones globales
        function agregarProductos() {

            document.getElementById('nuevoProductoNombre').value = '';
            document.getElementById('nuevoProductoDescripcion').value = '';
            
            const modal = new bootstrap.Modal(document.getElementById('agregarProductoModal'));
            modal.show();
        }
        
        function desactivarProductosSeleccionados() {
            const checkboxes = document.querySelectorAll('.producto-checkbox:checked');
            if (checkboxes.length === 0) {
                alert('Por favor seleccione al menos un producto');
                return;
            }
            
            const productIds = Array.from(checkboxes).map(cb => cb.value);
            alert('Desactivar productos seleccionados: ' + productIds.join(', ') + ' - Por implementar');
        }
        
        // Funciones para acciones por fila
        function editarProducto(productoId, servicioId, subservicioId, nombreProducto, nombreServicio, nombreSubservicio) {
            document.getElementById('editProductoId').value = productoId;
            document.getElementById('editServicioId').value = servicioId;
            document.getElementById('editSubservicioId').value = subservicioId;
            document.getElementById('editNombreProducto').value = nombreProducto;
            document.getElementById('editNombreServicio').value = nombreServicio;
            document.getElementById('editNombreSubservicio').value = nombreSubservicio;
            
            const modal = new bootstrap.Modal(document.getElementById('editarModal'));
            modal.show();
        }
        
        function guardarCambios() {
            const productoId = document.getElementById('editProductoId').value;
            const servicioId = document.getElementById('editServicioId').value;
            const subservicioId = document.getElementById('editSubservicioId').value;
            const nombreProducto = document.getElementById('editNombreProducto').value;
            const nombreServicio = document.getElementById('editNombreServicio').value;
            const nombreSubservicio = document.getElementById('editNombreSubservicio').value;
            
            fetch(contextPath + 'campanas/actualizar-producto-servicio', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    productoId: productoId,
                    servicioId: servicioId,
                    subservicioId: subservicioId,
                    nombreProducto: nombreProducto,
                    nombreServicio: nombreServicio,
                    nombreSubservicio: nombreSubservicio
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Cambios guardados exitosamente');
                    location.reload();
                } else {
                    alert('Error: ' + (data.error || 'No se pudieron guardar los cambios'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al procesar la solicitud');
            });
        }
        
        function agregarServicio(productoId) {
            document.getElementById('servicioProductoId').value = productoId;
            document.getElementById('nuevoServicioNombre').value = '';
            document.getElementById('nuevoServicioDescripcion').value = '';
            
            const modal = new bootstrap.Modal(document.getElementById('agregarServicioModal'));
            modal.show();
        }
        
        function crearProducto(){
               // --- LÓGICA PARA OBTENER EL ID DESDE LA URL ---
             const pathSegments = window.location.pathname.split('/'); // Divide la ruta por las barras '/'
             const campanaId = pathSegments.pop() || pathSegments.pop(); // Obtiene el último segmento (que debería ser el ID)
          // Se usa .pop() dos veces por si la URL termina con una barra, ej. /editar/2/
            const nombreP = document.getElementById('nuevoProductoNombre').value;
            const descripcionP = document.getElementById('nuevoProductoDescripcion').value;
              if (!nombreP.trim()) {
                alert('Por favor ingrese un nombre para el Producto');
                return;
            }
             fetch(contextPath + 'campanas/crear-producto', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    nombre: nombreP,
                    descripcion: descripcionP,
                    campanaId: campanaId
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Producto creado exitosamente');
                    location.reload();
                } else {
                    alert('Error: ' + (data.error || 'No se pudo crear el producto'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al procesar la solicitud');
            });
            
        }
        function crearServicio() {
            const productoId = document.getElementById('servicioProductoId').value;
            const nombre = document.getElementById('nuevoServicioNombre').value;
            const descripcion = document.getElementById('nuevoServicioDescripcion').value;
            
            if (!nombre.trim()) {
                alert('Por favor ingrese un nombre para el servicio');
                return;
            }
            
            fetch(contextPath +'campanas/crear-servicio', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    productoId: productoId,
                    nombre: nombre,
                    descripcion: descripcion
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Servicio creado exitosamente');
                    location.reload();
                } else {
                    alert('Error: ' + (data.error || 'No se pudo crear el servicio'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al procesar la solicitud');
            });
        }
        
        function agregarSubservicio(productoId, servicioId) {
            document.getElementById('subservicioServicioId').value = servicioId;
            document.getElementById('subservicioProductoId').value = productoId;
            document.getElementById('nuevoSubservicioNombre').value = '';
            document.getElementById('nuevoSubservicioDescripcion').value = '';
            
            const modal = new bootstrap.Modal(document.getElementById('agregarSubservicioModal'));
            modal.show();
        }
        
        function crearSubservicio() {
            const servicioId = document.getElementById('subservicioServicioId').value;
            const nombre = document.getElementById('nuevoSubservicioNombre').value;
            const descripcion = document.getElementById('nuevoSubservicioDescripcion').value;
            
            if (!nombre.trim()) {
                alert('Por favor ingrese un nombre para el subservicio');
                return;
            }
            
            fetch(contextPath +'campanas/crear-subservicio', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    servicioId: servicioId,
                    nombre: nombre,
                    descripcion: descripcion
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Subservicio creado exitosamente');
                    location.reload();
                } else {
                    alert('Error: ' + (data.error || 'No se pudo crear el subservicio'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al procesar la solicitud');
            });
        }
        
        function desactivarProducto(id) {
            if (!confirm('¿Está seguro de desactivar este producto?')) {
                return;
            }
            
            fetch(contextPath +'campanas/desactivar-producto/' + id, {
                method: 'POST'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Producto desactivado exitosamente');
                    location.reload();
                } else {
                    alert('Error: ' + (data.error || 'No se pudo desactivar el producto'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al procesar la solicitud');
            });
        }
        
        // Funciones de filtrado
        function filtrarProductos() {
            const filtro = document.getElementById('filtroGlobal').value.toLowerCase();
            const filas = document.querySelectorAll('#productosTableBody tr');
            let visibles = 0;
            
            filas.forEach(fila => {
                if (fila.id === 'noResultadosMsg') return;
                
                const producto = fila.cells[1]?.textContent.toLowerCase() || '';
                const servicio = fila.cells[2]?.textContent.toLowerCase() || '';
                const subservicio = fila.cells[3]?.textContent.toLowerCase() || '';
                
                if (producto.includes(filtro) || servicio.includes(filtro) || subservicio.includes(filtro)) {
                    fila.style.display = '';
                    visibles++;
                } else {
                    fila.style.display = 'none';
                }
            });
            
            document.getElementById('contadorFiltrados').textContent = visibles;
            
            if (visibles === 0 && filtro !== '') {
                mostrarMensajeNoResultados();
            } else {
                ocultarMensajeNoResultados();
            }
        }
        
        function limpiarFiltro() {
            document.getElementById('filtroGlobal').value = '';
            filtrarProductos();
        }
        
        function actualizarContadorProductos() {
            const filas = document.querySelectorAll('#productosTableBody tr');
            let total = 0;
            filas.forEach(fila => {
                if (fila.id !== 'noResultadosMsg' && !fila.querySelector('.empty-state')) {
                    total++;
                }
            });
            document.getElementById('totalProductos').textContent = total;
            document.getElementById('contadorFiltrados').textContent = total;
        }
        
        function mostrarMensajeNoResultados() {
            if (!document.getElementById('noResultadosMsg')) {
                const tbody = document.getElementById('productosTableBody');
                const tr = document.createElement('tr');
                tr.id = 'noResultadosMsg';
                tr.innerHTML = '<td colspan="6" class="text-center py-4">' +
                    '<i class="fas fa-search fa-3x text-muted mb-3"></i>' +
                    '<h5 class="text-muted">No se encontraron productos que coincidan con el filtro</h5>' +
                    '<p class="text-muted">Intente con otros términos de búsqueda</p>' +
                    '</td>';
                tbody.appendChild(tr);
            }
        }
        
        function ocultarMensajeNoResultados() {
            const msg = document.getElementById('noResultadosMsg');
            if (msg) {
                msg.remove();
            }
        }
        
        // Inicialización al cargar la página
        document.addEventListener('DOMContentLoaded', function() {
            const cards = document.querySelectorAll('.card, .global-actions, .filter-section');
            cards.forEach((card, index) => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';
                setTimeout(() => {
                    card.style.transition = 'all 0.5s ease';
                    card.style.opacity = '1';
                    card.style.transform = 'translateY(0)';
                }, index * 100);
            });
            
            actualizarContadorProductos();
        });
