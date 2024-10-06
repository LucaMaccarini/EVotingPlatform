-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mar 08, 2022 alle 16:08
-- Versione del server: 10.4.22-MariaDB
-- Versione PHP: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `e_voting_platform`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `astenuti_referendum_con_quorum`
--

CREATE TABLE `astenuti_referendum_con_quorum` (
  `id_referendum` int(11) NOT NULL,
  `n_astenuti` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `astenuti_votazione_maggioranza_assoluta`
--

CREATE TABLE `astenuti_votazione_maggioranza_assoluta` (
  `id_sessione_di_votazione` int(11) NOT NULL,
  `n_astenuti` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `candidati_sessione_di_voto`
--

CREATE TABLE `candidati_sessione_di_voto` (
  `id_sessione_voto` int(11) NOT NULL,
  `nome_partito` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `candidati_sessione_di_voto`
--

INSERT INTO `candidati_sessione_di_voto` (`id_sessione_voto`, `nome_partito`) VALUES
(1, 'test partito 1'),
(1, 'test partito 2'),
(1, 'test partito 3'),
(2, 'test partito 1'),
(2, 'test partito 2'),
(2, 'test partito 3'),
(4, 'test partito 1'),
(4, 'test partito 2'),
(4, 'test partito 3');

-- --------------------------------------------------------

--
-- Struttura della tabella `diritti_referendum`
--

CREATE TABLE `diritti_referendum` (
  `id_referendum` int(11) NOT NULL,
  `utente` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `diritti_referendum`
--

INSERT INTO `diritti_referendum` (`id_referendum`, `utente`) VALUES
(1, 'alessia_bertoli'),
(1, 'alessio_verga');

-- --------------------------------------------------------

--
-- Struttura della tabella `diritti_sessioni_di_voto`
--

CREATE TABLE `diritti_sessioni_di_voto` (
  `id_sessione_di_voto` int(11) NOT NULL,
  `utente` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `diritti_sessioni_di_voto`
--

INSERT INTO `diritti_sessioni_di_voto` (`id_sessione_di_voto`, `utente`) VALUES
(1, 'alessia_bertoli'),
(1, 'alessio_verga'),
(2, 'alessia_bertoli'),
(2, 'alessio_verga'),
(4, 'alessia_bertoli'),
(4, 'alessio_verga'),
(4, 'Ester_Monaldo');

-- --------------------------------------------------------

--
-- Struttura della tabella `logs`
--

CREATE TABLE `logs` (
  `id` int(11) NOT NULL,
  `data_e_ora` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `soggetto` varchar(30) NOT NULL,
  `messaggio` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `logs`
--

INSERT INTO `logs` (`id`, `data_e_ora`, `soggetto`, `messaggio`) VALUES
(1, '2022-03-08 11:28:25', 'alessia_bertoli', 'ha votato alla votazione: (id: 1) test votazione ordinale | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(2, '2022-03-08 11:28:25', 'alessia_bertoli', 'ha votato alla votazione: (id: 2) test categorica con preferenza | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(3, '2022-03-08 11:28:25', 'alessia_bertoli', 'ha votato al referendum: (id: 1) test referendum 1 | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(4, '2022-03-08 11:28:25', 'alessio_verga', 'ha votato alla votazione: (id: 2) test categorica con preferenza | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(5, '2022-03-08 11:28:25', 'alessio_verga', 'ha votato al referendum: (id: 1) test referendum 1 | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(6, '2022-03-08 11:28:25', 'alessio_verga', 'ha votato alla votazione: (id: 1) test votazione ordinale | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(7, '2022-03-08 11:42:54', 'luca_maccarini', 'ha registrato una nuova votazione (id: 5) qegev | savv'),
(8, '2022-03-08 11:43:16', 'luca_maccarini', ' ha registrato un voto nella sessione di voto (id: 5) qegev | Creata da: luca_maccarini | Data termine: 8/3/2022 alle ore 12:44'),
(9, '2022-03-08 11:43:20', 'luca_maccarini', ' ha registrato un voto nella sessione di voto (id: 5) qegev | Creata da: luca_maccarini | Data termine: 8/3/2022 alle ore 12:44'),
(10, '2022-03-08 11:43:23', 'luca_maccarini', ' ha registrato un voto nella sessione di voto (id: 5) qegev | Creata da: luca_maccarini | Data termine: 8/3/2022 alle ore 12:44'),
(11, '2022-03-08 11:43:26', 'luca_maccarini', ' ha registrato un voto nella sessione di voto (id: 5) qegev | Creata da: luca_maccarini | Data termine: 8/3/2022 alle ore 12:44'),
(12, '2022-03-08 11:44:48', 'luca_maccarini', 'ha avviato il calcolo del vincitore per la sessione: qegev | Creata da: luca_maccarini | Data termine: 8/3/2022 alle ore 12:44'),
(13, '2022-03-08 11:48:29', 'luca_maccarini', 'ha registrato una nuova votazione (id: 4) votazione ordinale | questa è una votazione ordinale'),
(14, '2022-03-08 11:48:37', 'luca_maccarini', ' ha registrato un voto nella sessione di voto (id: 4) votazione ordinale | Creata da: luca_maccarini | Data termine: 8/3/2022 alle ore 12:49'),
(15, '2022-03-08 11:48:41', 'luca_maccarini', ' ha registrato un voto nella sessione di voto (id: 4) votazione ordinale | Creata da: luca_maccarini | Data termine: 8/3/2022 alle ore 12:49'),
(16, '2022-03-08 11:48:46', 'luca_maccarini', ' ha registrato un voto nella sessione di voto (id: 4) votazione ordinale | Creata da: luca_maccarini | Data termine: 8/3/2022 alle ore 12:49'),
(17, '2022-03-08 15:04:18', 'alessia_bertoli', 'ha votato alla votazione: (id: 1) test votazione ordinale | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(18, '2022-03-08 15:04:18', 'alessia_bertoli', 'ha votato alla votazione: (id: 2) test categorica con preferenza | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(19, '2022-03-08 15:04:18', 'alessia_bertoli', 'ha votato al referendum: (id: 1) test referendum 1 | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(20, '2022-03-08 15:04:18', 'alessio_verga', 'ha votato alla votazione: (id: 2) test categorica con preferenza | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(21, '2022-03-08 15:04:18', 'alessio_verga', 'ha votato al referendum: (id: 1) test referendum 1 | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(22, '2022-03-08 15:04:18', 'alessio_verga', 'ha votato alla votazione: (id: 1) test votazione ordinale | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(23, '2022-03-08 15:04:27', 'luca_maccarini', 'ha registrato un nuovo elettore con username test'),
(24, '2022-03-08 15:06:03', 'alessia_bertoli', 'ha votato alla votazione: (id: 1) test votazione ordinale | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(25, '2022-03-08 15:06:03', 'alessia_bertoli', 'ha votato alla votazione: (id: 2) test categorica con preferenza | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(26, '2022-03-08 15:06:03', 'alessia_bertoli', 'ha votato al referendum: (id: 1) test referendum 1 | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(27, '2022-03-08 15:06:03', 'alessio_verga', 'ha votato alla votazione: (id: 2) test categorica con preferenza | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(28, '2022-03-08 15:06:03', 'alessio_verga', 'ha votato al referendum: (id: 1) test referendum 1 | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(29, '2022-03-08 15:06:03', 'alessio_verga', 'ha votato alla votazione: (id: 1) test votazione ordinale | Creata da: luca_maccarini | Data termine: 18/3/2032 alle ore 11:11'),
(30, '2022-03-08 15:06:11', 'luca_maccarini', 'ha registrato un nuovo elettore con username test');

-- --------------------------------------------------------

--
-- Struttura della tabella `partiti`
--

CREATE TABLE `partiti` (
  `nome` varchar(30) NOT NULL,
  `descrizione` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `partiti`
--

INSERT INTO `partiti` (`nome`, `descrizione`) VALUES
('Nessuno', 'no_vincitore'),
('non_calcolato', 'non_calcolato'),
('test partito 1', 'descrizione 1'),
('test partito 2', 'descrizione 2'),
('test partito 3', 'descrizione 3');

-- --------------------------------------------------------

--
-- Struttura della tabella `persone`
--

CREATE TABLE `persone` (
  `id` int(11) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `cognome` varchar(30) NOT NULL,
  `partito` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `persone`
--

INSERT INTO `persone` (`id`, `nome`, `cognome`, `partito`) VALUES
(38, 'nome persona 1', 'cognome persona 1', 'test partito 1'),
(39, 'nome persona 2', 'cognome ersona 2', 'test partito 2'),
(40, 'nome persona 3', 'cognome persona 3', 'test partito 3'),
(41, 'nome persona 4', 'cognome persona 4', 'test partito 1'),
(42, 'nome persona 5', 'cognome persona 5', 'test partito 2'),
(43, 'nome persona 6', 'cognome persona 6', 'test partito 3');

-- --------------------------------------------------------

--
-- Struttura della tabella `sessioni_di_votazione`
--

CREATE TABLE `sessioni_di_votazione` (
  `id` int(11) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `descrizione` varchar(300) NOT NULL,
  `proprietario` varchar(30) NOT NULL,
  `modalita_voto` enum('voto_ordinale','voto_categorico','voto_categorico_con_preferenze') NOT NULL,
  `modalita_vincitore` enum('maggioranza','maggioranza_assoluta') NOT NULL,
  `data_termine` timestamp NOT NULL DEFAULT current_timestamp(),
  `vincitore` varchar(30) NOT NULL DEFAULT 'non_calcolato'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `sessioni_di_votazione`
--

INSERT INTO `sessioni_di_votazione` (`id`, `nome`, `descrizione`, `proprietario`, `modalita_voto`, `modalita_vincitore`, `data_termine`, `vincitore`) VALUES
(1, 'test votazione ordinale', 'descrizione voto ordinale', 'luca_maccarini', 'voto_ordinale', 'maggioranza', '2032-03-18 10:11:00', 'non_calcolato'),
(2, 'test categorica con preferenza', 'descrizione voto categorico con preferenza', 'luca_maccarini', 'voto_categorico_con_preferenze', 'maggioranza', '2032-03-18 10:11:00', 'non_calcolato'),
(3, 'test votazione con preferenze', '', 'luca_maccarini', 'voto_categorico_con_preferenze', 'maggioranza', '2022-03-07 11:30:00', 'test partito 1'),
(4, 'votazione ordinale', 'questa è una votazione ordinale', 'luca_maccarini', 'voto_ordinale', 'maggioranza_assoluta', '2022-03-08 11:49:00', 'non_calcolato');

-- --------------------------------------------------------

--
-- Struttura della tabella `sessioni_referendum`
--

CREATE TABLE `sessioni_referendum` (
  `id` int(11) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `quesito` varchar(300) NOT NULL,
  `proprietario` varchar(30) NOT NULL,
  `modalita_vincitore` enum('referendum_senza_quorum','referendum_con_quorum') NOT NULL,
  `data_termine` timestamp NOT NULL DEFAULT current_timestamp(),
  `vincitore` enum('Si','No','Nessuno','non_calcolato') NOT NULL DEFAULT 'non_calcolato',
  `n_si` int(11) NOT NULL DEFAULT 0,
  `n_no` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `sessioni_referendum`
--

INSERT INTO `sessioni_referendum` (`id`, `nome`, `quesito`, `proprietario`, `modalita_vincitore`, `data_termine`, `vincitore`, `n_si`, `n_no`) VALUES
(1, 'test referendum 1', 'quesito 1', 'luca_maccarini', 'referendum_senza_quorum', '2032-03-18 10:11:00', 'non_calcolato', 0, 0),
(2, 'test vittoria si', 'deve vincere il si', 'luca_maccarini', 'referendum_senza_quorum', '2022-03-07 11:28:00', 'Si', 2, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `utente_votato_referendum`
--

CREATE TABLE `utente_votato_referendum` (
  `username` varchar(30) NOT NULL,
  `id_referendum` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `utente_votato_sessione_di_votazione`
--

CREATE TABLE `utente_votato_sessione_di_votazione` (
  `username` varchar(30) NOT NULL,
  `id_sessione_di_voto` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `utenti`
--

CREATE TABLE `utenti` (
  `username` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `nome` varchar(20) NOT NULL,
  `cognome` varchar(20) NOT NULL,
  `is_maschio` tinyint(1) NOT NULL,
  `data_nascita` date NOT NULL DEFAULT current_timestamp(),
  `comune_nascita` varchar(20) NOT NULL,
  `provincia_nascita` char(2) NOT NULL,
  `codice_fiscale` char(16) NOT NULL,
  `password` varchar(128) NOT NULL,
  `is_admin` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `utenti`
--

INSERT INTO `utenti` (`username`, `email`, `nome`, `cognome`, `is_maschio`, `data_nascita`, `comune_nascita`, `provincia_nascita`, `codice_fiscale`, `password`, `is_admin`) VALUES
('alessia_bertoli', 'dvwdvev@gmail.com', 'alessia', 'bertoli', 0, '2003-03-14', 'bergamo', 'bg', 'BRTLSS03C54A794A', '7f844b4c6e0017ef0382e64aeab15c992e74dea7c2f71b6ab8bf62d082ca8e5d166c88eaddc2a42cf0c81370c0b6b5dab3a32e327ee2e1e163c10546bc2cab13', 0),
('alessio_verga', 'adwsbvsdbsdbbsvav@gmail.com', 'alessio', 'verga', 1, '2001-02-01', 'bergamo', 'BG', 'VRGLSS01B01A794Q', '7f844b4c6e0017ef0382e64aeab15c992e74dea7c2f71b6ab8bf62d082ca8e5d166c88eaddc2a42cf0c81370c0b6b5dab3a32e327ee2e1e163c10546bc2cab13', 0),
('ale_zolla', 'prova@gmail.com', 'alessandro', 'zolla', 1, '2002-02-06', 'osio sotto', 'BG', 'ZLLLSN02B06G160B', '7f844b4c6e0017ef0382e64aeab15c992e74dea7c2f71b6ab8bf62d082ca8e5d166c88eaddc2a42cf0c81370c0b6b5dab3a32e327ee2e1e163c10546bc2cab13', 1),
('Ester_Monaldo', 'obywonimaccheroni@gmail.com', 'Ester', 'Monaldo', 0, '2001-02-01', 'bergamo', 'BG', 'MNLSTR01B41A794V', '7f844b4c6e0017ef0382e64aeab15c992e74dea7c2f71b6ab8bf62d082ca8e5d166c88eaddc2a42cf0c81370c0b6b5dab3a32e327ee2e1e163c10546bc2cab13', 0),
('luca_maccarini', 'lucamaccarini22@gmail.com', 'Luca', 'Maccarini', 1, '2000-12-08', 'bergamo', 'BG', 'MCCLCU00T08A794A', '7f844b4c6e0017ef0382e64aeab15c992e74dea7c2f71b6ab8bf62d082ca8e5d166c88eaddc2a42cf0c81370c0b6b5dab3a32e327ee2e1e163c10546bc2cab13', 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `voti_preferenze_sessione_di_voto`
--

CREATE TABLE `voti_preferenze_sessione_di_voto` (
  `id_sessione_di_votazione` int(11) NOT NULL,
  `id_persona` int(11) NOT NULL,
  `n_voti` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `voti_preferenze_sessione_di_voto`
--

INSERT INTO `voti_preferenze_sessione_di_voto` (`id_sessione_di_votazione`, `id_persona`, `n_voti`) VALUES
(3, 38, 0),
(3, 41, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `voti_sessione_di_voto`
--

CREATE TABLE `voti_sessione_di_voto` (
  `id_sessione_voto` int(11) NOT NULL,
  `partito` varchar(30) NOT NULL,
  `n_voti` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `voti_sessione_di_voto`
--

INSERT INTO `voti_sessione_di_voto` (`id_sessione_voto`, `partito`, `n_voti`) VALUES
(4, 'test partito 1', 1),
(4, 'test partito 2', 5),
(4, 'test partito 3', 3);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `astenuti_referendum_con_quorum`
--
ALTER TABLE `astenuti_referendum_con_quorum`
  ADD PRIMARY KEY (`id_referendum`);

--
-- Indici per le tabelle `astenuti_votazione_maggioranza_assoluta`
--
ALTER TABLE `astenuti_votazione_maggioranza_assoluta`
  ADD PRIMARY KEY (`id_sessione_di_votazione`);

--
-- Indici per le tabelle `candidati_sessione_di_voto`
--
ALTER TABLE `candidati_sessione_di_voto`
  ADD PRIMARY KEY (`id_sessione_voto`,`nome_partito`),
  ADD KEY `partito_chiave` (`nome_partito`);

--
-- Indici per le tabelle `diritti_referendum`
--
ALTER TABLE `diritti_referendum`
  ADD PRIMARY KEY (`id_referendum`,`utente`),
  ADD KEY `utente_foreign` (`utente`);

--
-- Indici per le tabelle `diritti_sessioni_di_voto`
--
ALTER TABLE `diritti_sessioni_di_voto`
  ADD PRIMARY KEY (`id_sessione_di_voto`,`utente`),
  ADD KEY `utente` (`utente`);

--
-- Indici per le tabelle `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `soggetto` (`soggetto`);

--
-- Indici per le tabelle `partiti`
--
ALTER TABLE `partiti`
  ADD PRIMARY KEY (`nome`);

--
-- Indici per le tabelle `persone`
--
ALTER TABLE `persone`
  ADD PRIMARY KEY (`id`),
  ADD KEY `partito_foreign` (`partito`);

--
-- Indici per le tabelle `sessioni_di_votazione`
--
ALTER TABLE `sessioni_di_votazione`
  ADD PRIMARY KEY (`id`),
  ADD KEY `proprieterio_foreign` (`proprietario`),
  ADD KEY `partito_foreignkey` (`vincitore`);

--
-- Indici per le tabelle `sessioni_referendum`
--
ALTER TABLE `sessioni_referendum`
  ADD PRIMARY KEY (`id`),
  ADD KEY `proprieterio_refrendum_foreign` (`proprietario`);

--
-- Indici per le tabelle `utente_votato_referendum`
--
ALTER TABLE `utente_votato_referendum`
  ADD PRIMARY KEY (`username`,`id_referendum`),
  ADD KEY `sessione_referendum` (`id_referendum`);

--
-- Indici per le tabelle `utente_votato_sessione_di_votazione`
--
ALTER TABLE `utente_votato_sessione_di_votazione`
  ADD PRIMARY KEY (`username`,`id_sessione_di_voto`),
  ADD KEY `sessione_di_votazione_id` (`id_sessione_di_voto`);

--
-- Indici per le tabelle `utenti`
--
ALTER TABLE `utenti`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `codice_fiscale` (`codice_fiscale`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indici per le tabelle `voti_preferenze_sessione_di_voto`
--
ALTER TABLE `voti_preferenze_sessione_di_voto`
  ADD PRIMARY KEY (`id_sessione_di_votazione`,`id_persona`);

--
-- Indici per le tabelle `voti_sessione_di_voto`
--
ALTER TABLE `voti_sessione_di_voto`
  ADD PRIMARY KEY (`id_sessione_voto`,`partito`),
  ADD KEY `partito_chiaveesterna` (`partito`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `logs`
--
ALTER TABLE `logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT per la tabella `persone`
--
ALTER TABLE `persone`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT per la tabella `sessioni_di_votazione`
--
ALTER TABLE `sessioni_di_votazione`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT per la tabella `sessioni_referendum`
--
ALTER TABLE `sessioni_referendum`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `astenuti_votazione_maggioranza_assoluta`
--
ALTER TABLE `astenuti_votazione_maggioranza_assoluta`
  ADD CONSTRAINT `sessione_di_votazione_foreign` FOREIGN KEY (`id_sessione_di_votazione`) REFERENCES `sessioni_di_votazione` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `candidati_sessione_di_voto`
--
ALTER TABLE `candidati_sessione_di_voto`
  ADD CONSTRAINT `id_sessione` FOREIGN KEY (`id_sessione_voto`) REFERENCES `sessioni_di_votazione` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `partito_chiave` FOREIGN KEY (`nome_partito`) REFERENCES `partiti` (`nome`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Limiti per la tabella `diritti_referendum`
--
ALTER TABLE `diritti_referendum`
  ADD CONSTRAINT `referendum_foreign` FOREIGN KEY (`id_referendum`) REFERENCES `sessioni_referendum` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `utente_foreign` FOREIGN KEY (`utente`) REFERENCES `utenti` (`username`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Limiti per la tabella `diritti_sessioni_di_voto`
--
ALTER TABLE `diritti_sessioni_di_voto`
  ADD CONSTRAINT `sessione` FOREIGN KEY (`id_sessione_di_voto`) REFERENCES `sessioni_di_votazione` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `utente` FOREIGN KEY (`utente`) REFERENCES `utenti` (`username`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Limiti per la tabella `persone`
--
ALTER TABLE `persone`
  ADD CONSTRAINT `partito_foreign` FOREIGN KEY (`partito`) REFERENCES `partiti` (`nome`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Limiti per la tabella `sessioni_di_votazione`
--
ALTER TABLE `sessioni_di_votazione`
  ADD CONSTRAINT `partito_foreignkey` FOREIGN KEY (`vincitore`) REFERENCES `partiti` (`nome`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `proprieterio_foreign` FOREIGN KEY (`proprietario`) REFERENCES `utenti` (`username`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Limiti per la tabella `sessioni_referendum`
--
ALTER TABLE `sessioni_referendum`
  ADD CONSTRAINT `proprieterio_refrendum_foreign` FOREIGN KEY (`proprietario`) REFERENCES `utenti` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `utente_votato_referendum`
--
ALTER TABLE `utente_votato_referendum`
  ADD CONSTRAINT `sessione_referendum` FOREIGN KEY (`id_referendum`) REFERENCES `sessioni_referendum` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `utente_vota` FOREIGN KEY (`username`) REFERENCES `utenti` (`username`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Limiti per la tabella `utente_votato_sessione_di_votazione`
--
ALTER TABLE `utente_votato_sessione_di_votazione`
  ADD CONSTRAINT `sessione_di_votazione_id` FOREIGN KEY (`id_sessione_di_voto`) REFERENCES `sessioni_di_votazione` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `utente_chiave_esterna` FOREIGN KEY (`username`) REFERENCES `utenti` (`username`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Limiti per la tabella `voti_sessione_di_voto`
--
ALTER TABLE `voti_sessione_di_voto`
  ADD CONSTRAINT `id_sessione_vot` FOREIGN KEY (`id_sessione_voto`) REFERENCES `sessioni_di_votazione` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `partito_chiaveesterna` FOREIGN KEY (`partito`) REFERENCES `partiti` (`nome`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
