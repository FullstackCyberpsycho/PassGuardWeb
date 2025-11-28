-- 1. Убираем default (nextval(...)) у user_id
ALTER TABLE passwords
    ALTER COLUMN user_id DROP DEFAULT;

-- 2. Удаляем старый foreign key, если он есть
ALTER TABLE passwords
    DROP CONSTRAINT IF EXISTS passwords_user_id_fkey;

-- 3. Теперь можно удалить последовательность, если она существует
DROP SEQUENCE IF EXISTS passwords_user_id_seq CASCADE;

-- 4. Меняем тип user_id на INTEGER
ALTER TABLE passwords
    ALTER COLUMN user_id TYPE INTEGER;

-- 5. Создаём внешний ключ заново
ALTER TABLE passwords
    ADD CONSTRAINT passwords_user_id_fkey
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE;
