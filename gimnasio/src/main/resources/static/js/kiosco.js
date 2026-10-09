const campoDni = document.getElementById("dni");

document.querySelectorAll("[data-tecla]").forEach(function (boton) {
    boton.addEventListener("click", function () {
        if (campoDni.value.length < 8) {
            campoDni.value += boton.dataset.tecla;
        }
    });
});

document.getElementById("borrar").addEventListener("click", function () {
    campoDni.value = campoDni.value.slice(0, -1);
});

document.getElementById("limpiar").addEventListener("click", function () {
    campoDni.value = "";
});
