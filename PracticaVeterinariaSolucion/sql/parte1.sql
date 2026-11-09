USE veterinaria;

DROP FUNCTION IF EXISTS citas_pendientes;
DROP PROCEDURE IF EXISTS ficha_mascota;
DROP PROCEDURE IF EXISTS anular_citas_dia;
DROP PROCEDURE IF EXISTS aplicar_descuento;

DELIMITER //

-- ---------------------------------------------------------------------
--  1.1 Número de citas pendientes de una mascota
-- ---------------------------------------------------------------------

CREATE FUNCTION citas_pendientes(p_mascota INT) RETURNS INT
READS SQL DATA
BEGIN
    DECLARE v_pendientes INT;

    SELECT COUNT(*) INTO v_pendientes
      FROM citas
     WHERE id_mascota = p_mascota
       AND estado = 'PENDIENTE';

    RETURN v_pendientes;
END //

-- ---------------------------------------------------------------------
--  1.2 Ficha de una mascota: nombre del dueño y total de citas
-- ---------------------------------------------------------------------

CREATE PROCEDURE ficha_mascota(IN  p_mascota       INT,
                               OUT p_nombre_dueno  VARCHAR(60),
                               OUT p_num_citas     INT)
BEGIN
    SET p_nombre_dueno = NULL;

    SELECT c.nombre INTO p_nombre_dueno
      FROM mascotas m
      JOIN clientes c ON c.id_cliente = m.id_cliente
     WHERE m.id_mascota = p_mascota;

    IF p_nombre_dueno IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La mascota no existe';
    END IF;

    SELECT COUNT(*) INTO p_num_citas
      FROM citas
     WHERE id_mascota = p_mascota;
END //

-- ---------------------------------------------------------------------
--  1.3 Anular las citas pendientes de un veterinario en un día
-- ---------------------------------------------------------------------

CREATE PROCEDURE anular_citas_dia(IN  p_veterinario  INT,
                                  IN  p_fecha        DATE,
                                  OUT p_anuladas     INT)
BEGIN
    DECLARE v_existe INT;

    SELECT COUNT(*) INTO v_existe
      FROM veterinarios
     WHERE id_veterinario = p_veterinario;

    IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El veterinario no existe';
    END IF;

    UPDATE citas
       SET estado = 'ANULADA'
     WHERE id_veterinario = p_veterinario
       AND DATE(fecha_hora) = p_fecha
       AND estado = 'PENDIENTE';

    SET p_anuladas = ROW_COUNT();
END //

-- ---------------------------------------------------------------------
--  1.4 Descuento del 10 % si el cliente tiene 3 o más mascotas
-- ---------------------------------------------------------------------

CREATE PROCEDURE aplicar_descuento(INOUT p_precio   DECIMAL(7,2),
                                   IN    p_cliente  INT)
BEGIN
    DECLARE v_num_mascotas INT;

    SELECT COUNT(*) INTO v_num_mascotas
      FROM mascotas
     WHERE id_cliente = p_cliente;

    IF v_num_mascotas >= 3 THEN
        SET p_precio = ROUND(p_precio * 0.90, 2);
    END IF;
END //

DELIMITER ;

-- ---------------------------------------------------------------------
--  Pruebas
-- ---------------------------------------------------------------------
-- SELECT citas_pendientes(1);                                -- 1
-- CALL ficha_mascota(1, @dueno, @num); SELECT @dueno, @num;  -- Carmen Díaz, 2
-- CALL ficha_mascota(99, @dueno, @num);                      -- Error 45000: La mascota no existe
-- SET @p = 35; CALL aplicar_descuento(@p, 1); SELECT @p;     -- 31.50
-- SET @p = 35; CALL aplicar_descuento(@p, 2); SELECT @p;     -- 35.00
-- CALL anular_citas_dia(1, '2026-11-10', @n); SELECT @n;     -- 3 (después recargar veterinaria.sql)
