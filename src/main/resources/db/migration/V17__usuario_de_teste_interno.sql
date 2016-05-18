/**
 adicionado por clayton santos da silva para testes internos
**/
DELETE FROM public.usuarios WHERE cpf = '08680630705';
INSERT INTO public.usuarios (id, cpf, senha, papel_id, servidor, habilitado, siorg, nome, siape, email_primario, email_secundario) VALUES (4, '08680630705', '$2a$06$Il/rDKqdXfpfjplDdM2CFuN04qOlOdNn.WgMZt/8aL1tvLtFqD5lG', 1, false, true, 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/61283', 'Editor de Serviços', null, 'clayton.silva@prodest.es.gov.br', '');
