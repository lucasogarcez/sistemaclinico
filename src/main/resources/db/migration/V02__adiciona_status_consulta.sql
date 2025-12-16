ALTER TABLE consulta
    ADD COLUMN status VARCHAR(20) DEFAULT 'AGENDADO',
    ADD COLUMN peso NUMERIC(5, 2),
    ADD COLUMN altura NUMERIC(5, 2),
    ADD COLUMN pressao_arterial VARCHAR(10),
    ADD COLUMN temperatura VARCHAR(10),
    ADD COLUMN observacoes TEXT,
    ADD COLUMN receita TEXT,
    ADD COLUMN solicitacao_exames TEXT,
    ADD COLUMN desfecho VARCHAR(20);