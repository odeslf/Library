-- Adiciona a coluna para a URL da foto do autor na tabela 'author'
ALTER TABLE author
    ADD COLUMN photo_url VARCHAR(512);

-- Adiciona a coluna para a URL da capa do livro na tabela 'book'
ALTER TABLE book
    ADD COLUMN cover_photo_url VARCHAR(512);
