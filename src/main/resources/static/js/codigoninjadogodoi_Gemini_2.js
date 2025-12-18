const App = {
    // Configurações globais
    CONFIG: {
        seletores: {
            money: ".componentemoney",
            data: ".componentedata",
            hora: ".componentehora",
            confirm: "[hx-confirm]:not([data-confirm-ready])",
            buscaContainer: ".searchable-select-container:not([data-initialized])",
            msgInput: "mensagemSA2",
            tipoInput: "tipoSA2",
            intervaloInput: "intervaloSA2",
        },
        maskMoney: {
            prefix: "", suffix: "", fixed: true, fractionDigits: 2, decimalSeparator: ",", thousandsSeparator: ".", autoCompleteDecimal: true,
        },
        datePicker: {
            dateFormat: "dd/mm/yyyy",
            customWeekDays: ["Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"],
            customMonths: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
            customClearBTN: "Limpar", customCancelBTN: "Cancelar", theme: { theme_color: "rgb(31 41 55)" },
        },
        texto: { cancelar: "Cancelar", remover: "Remover", tituloConfirm: "Você tem certeza?", }
    },

    Toast: Swal.mixin({
        toast: true, position: "center-end", iconColor: "white", customClass: { popup: "colored-toast" }, showConfirmButton: false, timer: 3000, timerProgressBar: true, grow: "row",
        didOpen: (toast) => { toast.onmouseenter = Swal.stopTimer; toast.onmouseleave = Swal.resumeTimer; },
    }),

    init: function () {
        this.atualizarComponentes();
        this.registrarListenersGlobais();
    },

    atualizarComponentes: function () {
        this.prepararMoney();
        this.prepararData();
        this.prepararHora();
        this.prepararConfirmacoes();
        this.prepararBusca(); // AQUI ESTÁ A MÁGICA
        this.mostrarNotificacao();
    },

    registrarListenersGlobais: function () {
        if (typeof htmx !== 'undefined') {
            htmx.on("htmx:afterSettle", () => { this.atualizarComponentes(); });
        }
        
        // Fecha dropdowns ao clicar fora
        if (!document.body.dataset.clickOutsideRegistered) {
            document.addEventListener("click", (event) => {
                const activeDropdown = document.querySelector(".custom-select-dropdown:not(.hidden)");
                if (activeDropdown && !event.target.closest(".searchable-select-container")) {
                    activeDropdown.classList.add("hidden");
                }
            });
            document.body.dataset.clickOutsideRegistered = "true";
        }
    },

    prepararMoney: function () {
        document.querySelectorAll(this.CONFIG.seletores.money).forEach((input) => {
            SimpleMaskMoney.setMask(input, this.CONFIG.maskMoney);
            input.classList.remove("componentemoney");
        });
    },

    prepararData: function () {
        document.querySelectorAll(this.CONFIG.seletores.data).forEach((input) => {
            input.classList.remove("componentedata");
            let options = { ...this.CONFIG.datePicker, el: "#" + input.id };
            if (input.value) {
                const parts = input.value.split("/");
                options.selectedDate = new Date(+parts[2], parts[1] - 1, +parts[0]);
            }
            const datePicker = MCDatepicker.create(options);
            datePicker.onOpen(() => {
                if (input.value && input.value !== datePicker.getFormatedDate()) {
                    const parts = input.value.split("/");
                    datePicker.setFullDate(new Date(+parts[2], parts[1] - 1, +parts[0]));
                }
            });
        });
    },

    prepararHora: function () {
        document.querySelectorAll(this.CONFIG.seletores.hora).forEach((input) => {
            input.classList.remove("componentehora");
            const dialog = new mdDateTimePicker.default({ type: "time", mode: true, inner24: true, cancel: this.CONFIG.texto.cancelar, });
            input.addEventListener("click", () => dialog.toggle());
            dialog.trigger = input;
            input.addEventListener("onOk", () => { input.value = dialog.time.format("HH:mm"); });
        });
    },

    prepararConfirmacoes: function () {
        document.querySelectorAll(this.CONFIG.seletores.confirm).forEach((elemento) => {
            elemento.removeEventListener("htmx:confirm", this._dispararSweetAlert);
            elemento.addEventListener("htmx:confirm", this._dispararSweetAlert);
            elemento.setAttribute("data-confirm-ready", "true");
        });
    },

    _dispararSweetAlert: function (e) {
        e.preventDefault();
        Swal.fire({
            title: App.CONFIG.texto.tituloConfirm, text: e.detail.question, icon: "warning", showCancelButton: true,
            cancelButtonText: App.CONFIG.texto.cancelar, confirmButtonText: App.CONFIG.texto.remover, confirmButtonColor: "#3085d6",
        }).then((result) => {
            if (result.isConfirmed) e.detail.issueRequest(true);
        });
    },

    mostrarNotificacao: function () {
        const inputMsg = document.getElementById(this.CONFIG.seletores.msgInput);
        if (inputMsg && inputMsg.value) {
            const tipo = document.getElementById(this.CONFIG.seletores.tipoInput)?.value || 'success';
            const intervalo = document.getElementById(this.CONFIG.seletores.intervaloInput)?.value || 3000;
            this.Toast.fire({ icon: tipo, title: inputMsg.value, timer: intervalo });
            inputMsg.value = "";
        }
    },

    // --- LÓGICA DO SEU SISTEMA ANTIGO ---
    prepararBusca: function () {
        const containers = document.querySelectorAll(this.CONFIG.seletores.buscaContainer);

        containers.forEach((container) => {
            container.dataset.initialized = "true";
            const trigger = container.querySelector(".custom-select-trigger");
            const dropdown = container.querySelector(".custom-select-dropdown");
            const optionsList = container.querySelector(".options-list");
            const selectedValueSpan = container.querySelector(".selected-value-span");
            const hiddenInput = container.querySelector(".hidden-select-input");

            // 1. Abrir/Fechar
            if (trigger && dropdown) {
                trigger.addEventListener("click", (e) => {
                    document.querySelectorAll(".custom-select-dropdown:not(.hidden)").forEach(d => {
                        if (d !== dropdown) d.classList.add("hidden");
                    });
                    dropdown.classList.toggle("hidden");
                    e.stopPropagation();
                    // Foca no input de busca se abriu
                    if (!dropdown.classList.contains("hidden")) {
                        const searchInput = dropdown.querySelector('input');
                        if (searchInput) searchInput.focus();
                    }
                });
            }

            // 2. Selecionar Item (IMPORTANTE: Lê dataset.value)
            if (optionsList) {
                optionsList.addEventListener("click", (event) => {
                    const option = event.target.closest("li");
                    if (option) {
                        // Atualiza visual
                        if (selectedValueSpan) selectedValueSpan.textContent = option.textContent.trim();
                        // Atualiza valor oculto (O ID VEM DAQUI)
                        if (hiddenInput) hiddenInput.value = option.dataset.value;
                        
                        // Fecha
                        if (dropdown) dropdown.classList.add("hidden");
                    }
                });
            }
        });
    }
};

document.addEventListener("DOMContentLoaded", () => App.init());