var cod = prompt("Por favor, ingrese su COD Funcionario:");

const apiUrlAdmin = 'http://172.16.21.2:3333/api/londraPost/v1/personaLondra/obtenerDatos'; // API DE ADMINISTRATIVOS POR COD FUNCIONARIO

const adminData = {
    usuario: cod
};
const adminHeaders = {
    'Content-Type': 'application/json'
};

const adminOptions = {
    method: 'POST',
    headers: adminHeaders,
    body: JSON.stringify(adminData)
};

fetch(apiUrlAdmin, adminOptions)
    .then(response => {
        if (!response.ok || response.status !== 200) {
            throw new Error('Error en la solicitud de la API');
        }else{
            return response.json();
        }
        
    })
    .then(data => {
        // Trabajar con los datos JSON
        if (cod !== null && cod.trim() !== "") {
            console.log('Datos recibidos:', data);
            var nombreInput = document.querySelector('input[name="nombre"]');
            nombreInput.value = data.per_nombres;
            var ap_paternoInput = document.querySelector('input[name="ap_paterno"]');
            ap_paternoInput.value = data.per_ap_paterno;
            var ap_maternoInput = document.querySelector('input[name="ap_materno"]');
            ap_maternoInput.value = data.per_ap_materno;
            var ciInput = document.querySelector('input[name="ci"]');
            ciInput.value = data.per_num_doc;
            var celularInput = document.querySelector('input[name="celular"]');
            celularInput.value = data.perd_celular;
            var cod_funcionarioInput = document.querySelector('input[name="cod_funcionario"]');
            cod_funcionarioInput.value = data.per_id;
        }
    })
    .catch(error => {
        alert("No ingresó el COD Funcionario. Por favor, regístrelo de manera manual.");
        console.error('Error en la solicitud:', error);
    });
