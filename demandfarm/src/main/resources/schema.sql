alter table if exists gotfam_tree_entity drop constraint if exists gotfam_tree_entity_pkey;
drop table if exists gotfam_tree_entity cascade;
drop table if exists favorites cascade;
CREATE TABLE public.gotfam_tree_entity (
    id uuid NOT NULL,
	"data" jsonb NOT NULL,
	created_on timestamp(6) NOT NULL,
	updated_on timestamp(6) NOT NULL,
	CONSTRAINT gotfam_tree_entity_pkey PRIMARY KEY (id)
);
CREATE INDEX idxhouseName ON gotfam_tree_entity ((data->>'houseName'));

CREATE TABLE public.favorites (
	gotfam_tree_id uuid NOT NULL,
	id uuid NOT NULL,
	created_on timestamp(6) NOT NULL,
	updated_on timestamp(6) NOT NULL,
	CONSTRAINT favorites_pkey PRIMARY KEY (id)
);