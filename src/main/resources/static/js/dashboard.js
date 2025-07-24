    // Animaciones suaves al cargar la página
        document.addEventListener('DOMContentLoaded', function() {
            // Fade in de las cards
            const cards = document.querySelectorAll('.card, .stat-card');
            cards.forEach((card, index) => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';
                setTimeout(() => {
                    card.style.transition = 'all 0.5s ease';
                    card.style.opacity = '1';
                    card.style.transform = 'translateY(0)';
                }, index * 100);
            });
        });
        
        // Función para crear nueva campaña
        document.getElementById('btnCrearCampana').addEventListener('click', function() {
            const nombreCampana = document.getElementById('nombreCampana').value.trim();
            const form = document.getElementById('formNuevaCampana');
            
            // Validar campo
            if (!nombreCampana) {
                form.classList.add('was-validated');
                document.getElementById('nombreCampana').focus();
                return;
            }
            
            // Mostrar loading
            this.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Creando...';
            this.disabled = true;
            
            // Crear campaña via AJAX
            const formData = new FormData();
            formData.append('nombre', nombreCampana);
            
            fetch(form.action, {
                method: form.method,
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Éxito - recargar página
                    alert('Campaña creada exitosamente');
                    window.location.reload();
                } else {
                    // Error
                    alert('Error: ' + (data.error || 'No se pudo crear la campaña'));
                    // Restaurar botón
                    this.innerHTML = '<i class="fas fa-save me-2"></i>Crear Campaña';
                    this.disabled = false;
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error de conexión. Intente nuevamente.');
                // Restaurar botón
                this.innerHTML = '<i class="fas fa-save me-2"></i>Crear Campaña';
                this.disabled = false;
            });
        });
        
        function irA(url) {
            window.location.href = url;
        }
        
        function eliminarCampana(id) {
            if (confirm('¿Está seguro de que desea eliminar esta campaña?')) {
                alert('Función de eliminar campaña ID: ' + id + ' (por implementar)');
            }
        }
        
        // Limpiar modal al cerrarlo
        document.getElementById('modalNuevaCampana').addEventListener('hidden.bs.modal', function() {
            document.getElementById('formNuevaCampana').reset();
            document.getElementById('formNuevaCampana').classList.remove('was-validated');
            const btnCrear = document.getElementById('btnCrearCampana');
            btnCrear.innerHTML = '<i class="fas fa-save me-2"></i>Crear Campaña';
            btnCrear.disabled = false;
        });
