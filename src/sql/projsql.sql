--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: unaccent; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS unaccent WITH SCHEMA public;


--
-- Name: EXTENSION unaccent; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION unaccent IS 'text search dictionary that removes accents';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: artista; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.artista (
    id integer NOT NULL,
    genero character varying(100) NOT NULL,
    nome_artistico character varying(255)
);


ALTER TABLE public.artista OWNER TO lucas_ic7p28;

--
-- Name: historico_busca; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.historico_busca (
    id integer NOT NULL,
    usuario_id integer,
    termo_busca character varying(255) NOT NULL
);


ALTER TABLE public.historico_busca OWNER TO lucas_ic7p28;

--
-- Name: historico_busca_id_seq; Type: SEQUENCE; Schema: public; Owner: lucas_ic7p28
--

CREATE SEQUENCE public.historico_busca_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.historico_busca_id_seq OWNER TO lucas_ic7p28;

--
-- Name: historico_busca_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: lucas_ic7p28
--

ALTER SEQUENCE public.historico_busca_id_seq OWNED BY public.historico_busca.id;


--
-- Name: musica; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.musica (
    id integer NOT NULL,
    artista_id integer NOT NULL,
    nome character varying(255) NOT NULL,
    genero character varying(100) NOT NULL,
    album character varying(255) NOT NULL
);


ALTER TABLE public.musica OWNER TO lucas_ic7p28;

--
-- Name: musica_curtida; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.musica_curtida (
    id integer NOT NULL,
    usuario_id integer NOT NULL,
    musica_id integer NOT NULL
);


ALTER TABLE public.musica_curtida OWNER TO lucas_ic7p28;

--
-- Name: musica_curtida_id_seq; Type: SEQUENCE; Schema: public; Owner: lucas_ic7p28
--

CREATE SEQUENCE public.musica_curtida_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.musica_curtida_id_seq OWNER TO lucas_ic7p28;

--
-- Name: musica_curtida_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: lucas_ic7p28
--

ALTER SEQUENCE public.musica_curtida_id_seq OWNED BY public.musica_curtida.id;


--
-- Name: musica_descurtida; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.musica_descurtida (
    id integer NOT NULL,
    usuario_id integer NOT NULL,
    musica_id integer NOT NULL
);


ALTER TABLE public.musica_descurtida OWNER TO lucas_ic7p28;

--
-- Name: musica_descurtida_id_seq; Type: SEQUENCE; Schema: public; Owner: lucas_ic7p28
--

CREATE SEQUENCE public.musica_descurtida_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.musica_descurtida_id_seq OWNER TO lucas_ic7p28;

--
-- Name: musica_descurtida_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: lucas_ic7p28
--

ALTER SEQUENCE public.musica_descurtida_id_seq OWNED BY public.musica_descurtida.id;


--
-- Name: musica_id_seq; Type: SEQUENCE; Schema: public; Owner: lucas_ic7p28
--

CREATE SEQUENCE public.musica_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.musica_id_seq OWNER TO lucas_ic7p28;

--
-- Name: musica_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: lucas_ic7p28
--

ALTER SEQUENCE public.musica_id_seq OWNED BY public.musica.id;


--
-- Name: pessoa; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.pessoa (
    id integer NOT NULL,
    nome character varying(255) NOT NULL
);


ALTER TABLE public.pessoa OWNER TO lucas_ic7p28;

--
-- Name: pessoa_id_seq; Type: SEQUENCE; Schema: public; Owner: lucas_ic7p28
--

CREATE SEQUENCE public.pessoa_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pessoa_id_seq OWNER TO lucas_ic7p28;

--
-- Name: pessoa_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: lucas_ic7p28
--

ALTER SEQUENCE public.pessoa_id_seq OWNED BY public.pessoa.id;


--
-- Name: playlist; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.playlist (
    id integer NOT NULL,
    usuario_id integer NOT NULL,
    nome character varying(255) NOT NULL
);


ALTER TABLE public.playlist OWNER TO lucas_ic7p28;

--
-- Name: playlist_id_seq; Type: SEQUENCE; Schema: public; Owner: lucas_ic7p28
--

CREATE SEQUENCE public.playlist_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.playlist_id_seq OWNER TO lucas_ic7p28;

--
-- Name: playlist_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: lucas_ic7p28
--

ALTER SEQUENCE public.playlist_id_seq OWNED BY public.playlist.id;


--
-- Name: playlist_musica; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.playlist_musica (
    playlist_id integer NOT NULL,
    musica_id integer NOT NULL
);


ALTER TABLE public.playlist_musica OWNER TO lucas_ic7p28;

--
-- Name: usuario; Type: TABLE; Schema: public; Owner: lucas_ic7p28
--

CREATE TABLE public.usuario (
    id integer NOT NULL,
    email character varying(255) NOT NULL,
    senha character varying(255) NOT NULL
);


ALTER TABLE public.usuario OWNER TO lucas_ic7p28;

--
-- Name: historico_busca id; Type: DEFAULT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.historico_busca ALTER COLUMN id SET DEFAULT nextval('public.historico_busca_id_seq'::regclass);


--
-- Name: musica id; Type: DEFAULT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica ALTER COLUMN id SET DEFAULT nextval('public.musica_id_seq'::regclass);


--
-- Name: musica_curtida id; Type: DEFAULT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_curtida ALTER COLUMN id SET DEFAULT nextval('public.musica_curtida_id_seq'::regclass);


--
-- Name: musica_descurtida id; Type: DEFAULT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_descurtida ALTER COLUMN id SET DEFAULT nextval('public.musica_descurtida_id_seq'::regclass);


--
-- Name: pessoa id; Type: DEFAULT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.pessoa ALTER COLUMN id SET DEFAULT nextval('public.pessoa_id_seq'::regclass);


--
-- Name: playlist id; Type: DEFAULT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.playlist ALTER COLUMN id SET DEFAULT nextval('public.playlist_id_seq'::regclass);


--
-- Data for Name: artista; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.artista VALUES (91, 'MPB', 'Roberta Campos');
INSERT INTO public.artista VALUES (92, 'Samba', 'Thiaguinho');
INSERT INTO public.artista VALUES (93, 'Sertanejo', 'Marília Mendonça');
INSERT INTO public.artista VALUES (98, 'Pop', 'Artista Teste');
INSERT INTO public.artista VALUES (103, 'MPB/Samba/Pop', 'Seu Jorge');
INSERT INTO public.artista VALUES (104, 'Rap/Hip-Hop', 'Racionais MC''s');
INSERT INTO public.artista VALUES (105, 'Pagode', 'Turma do Pagode');
INSERT INTO public.artista VALUES (106, 'Sertanejo', 'Jorge & Mateus');
INSERT INTO public.artista VALUES (107, 'Rock Nacional', 'Legião Urbana');


--
-- Data for Name: historico_busca; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.historico_busca VALUES (11, 90, 'MPB');
INSERT INTO public.historico_busca VALUES (12, 90, 'rock nacional');
INSERT INTO public.historico_busca VALUES (13, 90, 'sertanejo raiz');
INSERT INTO public.historico_busca VALUES (14, 90, 'Roberta Campos');
INSERT INTO public.historico_busca VALUES (15, 94, 'samba de raiz');
INSERT INTO public.historico_busca VALUES (16, 94, 'pagode anos 90');
INSERT INTO public.historico_busca VALUES (17, 94, 'Thiaguinho');
INSERT INTO public.historico_busca VALUES (18, 94, 'Marília Mendonça');
INSERT INTO public.historico_busca VALUES (19, 90, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (20, 94, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (21, 94, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (22, 94, 'pagode');
INSERT INTO public.historico_busca VALUES (23, 94, 'sertanejo');
INSERT INTO public.historico_busca VALUES (24, 94, 'rock');
INSERT INTO public.historico_busca VALUES (25, 94, 'musica');
INSERT INTO public.historico_busca VALUES (26, 94, 'roberta');
INSERT INTO public.historico_busca VALUES (27, 94, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (28, 94, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (29, 94, 'mpb');
INSERT INTO public.historico_busca VALUES (30, 94, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (31, 94, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (32, 94, 'mpb');
INSERT INTO public.historico_busca VALUES (33, 94, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (34, 94, 'sertanejo');
INSERT INTO public.historico_busca VALUES (35, 94, 'mpb');
INSERT INTO public.historico_busca VALUES (36, 99, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (37, 94, 'thiaguinho');
INSERT INTO public.historico_busca VALUES (38, 94, 'racionais');
INSERT INTO public.historico_busca VALUES (39, 94, 'diario');
INSERT INTO public.historico_busca VALUES (40, 94, 'diario de um detento');
INSERT INTO public.historico_busca VALUES (41, 94, 'Seu Jorge');
INSERT INTO public.historico_busca VALUES (42, 94, 'Racionais');
INSERT INTO public.historico_busca VALUES (43, 94, 'Racionais Mc''s');
INSERT INTO public.historico_busca VALUES (44, 94, 'racionais Mc''s');
INSERT INTO public.historico_busca VALUES (45, 94, 'Jorge e mateus');
INSERT INTO public.historico_busca VALUES (46, 94, 'Jorge e Mateus');
INSERT INTO public.historico_busca VALUES (47, 94, 'Jorge & Mateus');


--
-- Data for Name: musica; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.musica VALUES (67, 91, 'De Janeiro a Janeiro', 'MPB', 'Todo Caminho é Sorte');
INSERT INTO public.musica VALUES (68, 91, 'Abrigo', 'MPB', 'Todo Caminho é Sorte');
INSERT INTO public.musica VALUES (69, 92, 'Ousadia & Alegria', 'Samba', 'Ousadia & Alegria');
INSERT INTO public.musica VALUES (70, 92, 'Caraca, Muleke!', 'Samba', 'Hey, Mundo!');
INSERT INTO public.musica VALUES (71, 93, 'Infiel', 'Sertanejo', 'Marília Mendonça');
INSERT INTO public.musica VALUES (72, 93, 'De Quem é a Culpa?', 'Sertanejo', 'Realidade');
INSERT INTO public.musica VALUES (103, 103, 'Burguesinha', 'MPB/Samba', 'América Brasil');
INSERT INTO public.musica VALUES (104, 104, 'Negro Drama', 'Rap/Hip-Hop', 'Nada Como um Dia Após o Outro Dia');
INSERT INTO public.musica VALUES (105, 105, 'Lancinho', 'Pagode', 'O Som das Multidões');
INSERT INTO public.musica VALUES (106, 106, 'Amo Noite e Dia', 'Sertanejo', 'Ao Vivo em Jurerê');
INSERT INTO public.musica VALUES (107, 107, 'Tempo Perdido', 'Rock Nacional', 'Dois');
INSERT INTO public.musica VALUES (108, 103, 'Mina do Condomínio', 'MPB/Samba', 'América Brasil');
INSERT INTO public.musica VALUES (109, 103, 'Carolina', 'MPB', 'Samba Esporte Fino');
INSERT INTO public.musica VALUES (110, 104, 'Diário de um Detento', 'Rap/Hip-Hop', 'Sobrevivendo no Inferno');
INSERT INTO public.musica VALUES (111, 105, 'Camisa 10', 'Pagode', 'Camisa 10');
INSERT INTO public.musica VALUES (112, 105, 'Deixa em Off', 'Pagode', 'Mania do Brasil');
INSERT INTO public.musica VALUES (113, 106, 'Os Anjos Cantam', 'Sertanejo', 'Os Anjos Cantam');
INSERT INTO public.musica VALUES (114, 106, 'Pode Chorar', 'Sertanejo', 'Ao Vivo em Goiânia');
INSERT INTO public.musica VALUES (116, 107, 'Eduardo e Mônica', 'Rock Nacional', 'Dois');
INSERT INTO public.musica VALUES (117, 107, 'Pais e Filhos', 'Rock Nacional', 'As Quatro Estações');


--
-- Data for Name: musica_curtida; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.musica_curtida VALUES (20, 94, 71);
INSERT INTO public.musica_curtida VALUES (21, 94, 72);
INSERT INTO public.musica_curtida VALUES (23, 99, 69);
INSERT INTO public.musica_curtida VALUES (26, 94, 103);
INSERT INTO public.musica_curtida VALUES (27, 94, 108);
INSERT INTO public.musica_curtida VALUES (28, 94, 104);
INSERT INTO public.musica_curtida VALUES (29, 94, 110);
INSERT INTO public.musica_curtida VALUES (30, 94, 106);
INSERT INTO public.musica_curtida VALUES (31, 94, 113);
INSERT INTO public.musica_curtida VALUES (32, 94, 114);


--
-- Data for Name: musica_descurtida; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.musica_descurtida VALUES (5, 90, 67);
INSERT INTO public.musica_descurtida VALUES (6, 94, 70);
INSERT INTO public.musica_descurtida VALUES (7, 94, 67);
INSERT INTO public.musica_descurtida VALUES (8, 99, 70);
INSERT INTO public.musica_descurtida VALUES (9, 94, 69);


--
-- Data for Name: pessoa; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.pessoa VALUES (90, 'luanna');
INSERT INTO public.pessoa VALUES (91, 'Roberta Campos');
INSERT INTO public.pessoa VALUES (92, 'Thiaguinho');
INSERT INTO public.pessoa VALUES (93, 'Marília Mendonça');
INSERT INTO public.pessoa VALUES (94, 'lucas');
INSERT INTO public.pessoa VALUES (98, 'Pessoa Artista');
INSERT INTO public.pessoa VALUES (99, 'edna');
INSERT INTO public.pessoa VALUES (103, 'Jorge');
INSERT INTO public.pessoa VALUES (104, 'Mano Brown');
INSERT INTO public.pessoa VALUES (105, 'Turma do Pagode');
INSERT INTO public.pessoa VALUES (106, 'Jorge e Mateus');
INSERT INTO public.pessoa VALUES (107, 'Renato Russo');


--
-- Data for Name: playlist; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.playlist VALUES (30, 94, 'Sertanejo Vibes');
INSERT INTO public.playlist VALUES (32, 94, 'Melhores');


--
-- Data for Name: playlist_musica; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.playlist_musica VALUES (32, 71);
INSERT INTO public.playlist_musica VALUES (30, 71);
INSERT INTO public.playlist_musica VALUES (30, 114);
INSERT INTO public.playlist_musica VALUES (30, 113);
INSERT INTO public.playlist_musica VALUES (32, 107);
INSERT INTO public.playlist_musica VALUES (32, 111);


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: lucas_ic7p28
--

INSERT INTO public.usuario VALUES (90, 'luannagon1104@gmail.com', '1221');
INSERT INTO public.usuario VALUES (94, 'lucasvmasashi@gmail.com', '1910');
INSERT INTO public.usuario VALUES (99, 'edna@edna.com', '1234');


--
-- Name: historico_busca_id_seq; Type: SEQUENCE SET; Schema: public; Owner: lucas_ic7p28
--

SELECT pg_catalog.setval('public.historico_busca_id_seq', 47, true);


--
-- Name: musica_curtida_id_seq; Type: SEQUENCE SET; Schema: public; Owner: lucas_ic7p28
--

SELECT pg_catalog.setval('public.musica_curtida_id_seq', 32, true);


--
-- Name: musica_descurtida_id_seq; Type: SEQUENCE SET; Schema: public; Owner: lucas_ic7p28
--

SELECT pg_catalog.setval('public.musica_descurtida_id_seq', 9, true);


--
-- Name: musica_id_seq; Type: SEQUENCE SET; Schema: public; Owner: lucas_ic7p28
--

SELECT pg_catalog.setval('public.musica_id_seq', 76, true);


--
-- Name: pessoa_id_seq; Type: SEQUENCE SET; Schema: public; Owner: lucas_ic7p28
--

SELECT pg_catalog.setval('public.pessoa_id_seq', 107, true);


--
-- Name: playlist_id_seq; Type: SEQUENCE SET; Schema: public; Owner: lucas_ic7p28
--

SELECT pg_catalog.setval('public.playlist_id_seq', 33, true);


--
-- Name: artista artista_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.artista
    ADD CONSTRAINT artista_pkey PRIMARY KEY (id);


--
-- Name: historico_busca historico_busca_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.historico_busca
    ADD CONSTRAINT historico_busca_pkey PRIMARY KEY (id);


--
-- Name: musica_curtida musica_curtida_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_curtida
    ADD CONSTRAINT musica_curtida_pkey PRIMARY KEY (id);


--
-- Name: musica_curtida musica_curtida_usuario_id_musica_id_key; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_curtida
    ADD CONSTRAINT musica_curtida_usuario_id_musica_id_key UNIQUE (usuario_id, musica_id);


--
-- Name: musica_descurtida musica_descurtida_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_descurtida
    ADD CONSTRAINT musica_descurtida_pkey PRIMARY KEY (id);


--
-- Name: musica musica_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica
    ADD CONSTRAINT musica_pkey PRIMARY KEY (id);


--
-- Name: pessoa pessoa_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.pessoa
    ADD CONSTRAINT pessoa_pkey PRIMARY KEY (id);


--
-- Name: playlist_musica playlist_musica_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.playlist_musica
    ADD CONSTRAINT playlist_musica_pkey PRIMARY KEY (playlist_id, musica_id);


--
-- Name: playlist playlist_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.playlist
    ADD CONSTRAINT playlist_pkey PRIMARY KEY (id);


--
-- Name: usuario usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- Name: artista artista_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.artista
    ADD CONSTRAINT artista_id_fkey FOREIGN KEY (id) REFERENCES public.pessoa(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: musica_curtida fk_musica; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_curtida
    ADD CONSTRAINT fk_musica FOREIGN KEY (musica_id) REFERENCES public.musica(id) ON DELETE CASCADE;


--
-- Name: musica_descurtida fk_musica_descurtida; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_descurtida
    ADD CONSTRAINT fk_musica_descurtida FOREIGN KEY (musica_id) REFERENCES public.musica(id);


--
-- Name: musica_curtida fk_usuario; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_curtida
    ADD CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id) ON DELETE CASCADE;


--
-- Name: musica_descurtida fk_usuario_descurtida; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica_descurtida
    ADD CONSTRAINT fk_usuario_descurtida FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- Name: historico_busca historico_busca_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.historico_busca
    ADD CONSTRAINT historico_busca_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- Name: musica musica_artista_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.musica
    ADD CONSTRAINT musica_artista_id_fkey FOREIGN KEY (artista_id) REFERENCES public.artista(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: playlist_musica playlist_musica_musica_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.playlist_musica
    ADD CONSTRAINT playlist_musica_musica_id_fkey FOREIGN KEY (musica_id) REFERENCES public.musica(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: playlist_musica playlist_musica_playlist_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.playlist_musica
    ADD CONSTRAINT playlist_musica_playlist_id_fkey FOREIGN KEY (playlist_id) REFERENCES public.playlist(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: playlist playlist_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.playlist
    ADD CONSTRAINT playlist_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuario(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: usuario usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: lucas_ic7p28
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_id_fkey FOREIGN KEY (id) REFERENCES public.pessoa(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

