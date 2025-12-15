CREATE TABLE public.paciente
(
    codigo serial NOT NULL,
    nome text,
    cpf text,
    data_nascimento date,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);

CREATE TABLE public.medico
(
    codigo serial NOT NULL,
    nome text,
    crm text,
    status text DEFAULT 'ATIVO',
    PRIMARY KEY (codigo)
);

CREATE TABLE public.consulta
(
    codigo serial NOT NULL,
    data date,
    horario time,
    codigo_paciente integer,
    codigo_medico integer,
    PRIMARY KEY (codigo)
);

ALTER TABLE public.consulta
    ADD FOREIGN KEY (codigo_paciente)
    REFERENCES public.paciente (codigo)
    NOT VALID;


ALTER TABLE public.consulta
    ADD FOREIGN KEY (codigo_medico)
    REFERENCES public.medico (codigo)
    NOT VALID;