 document.getElementById('loginForm').addEventListener('submit', function() {
            const loginBtn = document.getElementById('loginBtn');
            loginBtn.classList.add('loading');
            loginBtn.disabled = true;
            
            // Reactivar el botón después de 5 segundos en caso de error
            setTimeout(() => {
                loginBtn.classList.remove('loading');
                loginBtn.disabled = false;
            }, 5000);
        });
        
        // Auto-focus en el campo usuario
        document.getElementById('username').focus();
