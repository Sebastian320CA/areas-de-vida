-- Datos de demostración (ficticios). Se cargan solo si no hay áreas registradas.

INSERT INTO living_areas (name, location, hectares, conservation_status, creation_date) VALUES
('Páramo de Rabanal', 'Ventaquemada, Boyacá', 1250.50, 'VULNERABLE', '2023-03-15'),
('Ribera Laguna de Tota', 'Aquitania, Boyacá', 420.00, 'AT_RISK', '2022-08-02'),
('Bosque de Viracachá', 'Viracachá, Boyacá', 86.75, 'IMPROVING', '2024-01-20'),
('Cuenca Alta del Río Jordán', 'Tunja, Boyacá', 310.20, 'ENDANGERED', '2021-11-09'),
('Reserva Santuario del Roble', 'Tunja, Boyacá', 58.40, 'STABLE', '2020-05-27');

INSERT INTO tree_species (species, genus, family, common_name) VALUES
('Quercus humboldtii', 'Quercus', 'Fagaceae', 'Roble andino'),
('Alnus acuminata', 'Alnus', 'Betulaceae', 'Aliso'),
('Polylepis quadrijuga', 'Polylepis', 'Rosaceae', 'Colorado'),
('Cedrela montana', 'Cedrela', 'Meliaceae', 'Cedro de montaña'),
('Retrophyllum rospigliosii', 'Retrophyllum', 'Podocarpaceae', 'Pino romerón');

INSERT INTO planting_activities (activity_date, activity_type, description, trees_planted, living_area_id, tree_id) VALUES
('2026-09-12', 'Siembra', 'Jornada comunitaria de siembra en zona de recarga hídrica.', 350, (SELECT id FROM living_areas WHERE name = 'Páramo de Rabanal'), (SELECT id FROM tree_species WHERE species = 'Polylepis quadrijuga')),
('2026-09-28', 'Siembra', 'Restauración de ronda hídrica con especies nativas.', 220, (SELECT id FROM living_areas WHERE name = 'Cuenca Alta del Río Jordán'), (SELECT id FROM tree_species WHERE species = 'Alnus acuminata')),
('2026-10-05', 'Mantenimiento', 'Plateo y fertilización orgánica de individuos jóvenes.', 0, (SELECT id FROM living_areas WHERE name = 'Bosque de Viracachá'), NULL),
('2026-10-19', 'Replante', 'Reposición de individuos que no sobrevivieron al verano.', 80, (SELECT id FROM living_areas WHERE name = 'Ribera Laguna de Tota'), (SELECT id FROM tree_species WHERE species = 'Quercus humboldtii')),
('2026-10-26', 'Siembra', 'Ampliación del corredor biológico.', 150, (SELECT id FROM living_areas WHERE name = 'Reserva Santuario del Roble'), (SELECT id FROM tree_species WHERE species = 'Cedrela montana'));

INSERT INTO forest_censuses (census_date, vegetation_coverage, ecosystem_state, observations, living_area_id) VALUES
('2026-08-10', 72.50, 'Bueno', 'Regeneración natural visible en el borde del bosque.', (SELECT id FROM living_areas WHERE name = 'Páramo de Rabanal')),
('2026-08-22', 48.00, 'Regular', 'Presión por ganadería extensiva en el límite oriental.', (SELECT id FROM living_areas WHERE name = 'Ribera Laguna de Tota')),
('2026-09-14', 81.30, 'Excelente', 'Buena supervivencia de los individuos sembrados.', (SELECT id FROM living_areas WHERE name = 'Bosque de Viracachá')),
('2026-09-30', 35.60, 'Deteriorado', 'Erosión en laderas, se recomienda barrera viva.', (SELECT id FROM living_areas WHERE name = 'Cuenca Alta del Río Jordán')),
('2026-10-15', 88.90, 'Excelente', 'Dosel cerrado y presencia de fauna silvestre.', (SELECT id FROM living_areas WHERE name = 'Reserva Santuario del Roble'));

INSERT INTO documentation (document_type, document_number, issue_date, issuing_authority, living_area_id) VALUES
('Resolución de declaratoria', 'RES-2023-0142', '2023-03-01', 'Corpoboyacá', (SELECT id FROM living_areas WHERE name = 'Páramo de Rabanal')),
('Concepto técnico', 'CT-2022-0587', '2022-07-18', 'Corpoboyacá', (SELECT id FROM living_areas WHERE name = 'Ribera Laguna de Tota')),
('Plan de manejo', 'PM-2024-0033', '2024-01-10', 'Alcaldía de Viracachá', (SELECT id FROM living_areas WHERE name = 'Bosque de Viracachá'));
