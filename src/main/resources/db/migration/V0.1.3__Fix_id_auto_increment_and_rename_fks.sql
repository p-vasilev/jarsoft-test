ALTER TABLE banner_category DROP CONSTRAINT
    banner_category_ibfk_1;
ALTER TABLE banner_category DROP CONSTRAINT
    banner_category_ibfk_2;
ALTER TABLE log DROP CONSTRAINT
    log_ibfk_1;
ALTER TABLE log DROP CONSTRAINT
    log_ibfk_2;
ALTER TABLE log_banner_category DROP CONSTRAINT
    log_banner_category_ibfk_1;
ALTER TABLE log_banner_category DROP CONSTRAINT
    log_banner_category_ibfk_2;

ALTER TABLE category
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE banner
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE log
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE user_agent
    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE banner_category
    ADD CONSTRAINT banner_category_banner_fk FOREIGN KEY (banner_id) REFERENCES banner(id);
ALTER TABLE banner_category
    ADD CONSTRAINT banner_category_category_fk FOREIGN KEY (category_id) REFERENCES category(id);
ALTER TABLE log
    ADD CONSTRAINT log_banner_fk FOREIGN KEY (banner_id) REFERENCES banner(id);
ALTER TABLE log
    ADD CONSTRAINT log_user_agent_fk FOREIGN KEY (user_agent_id) REFERENCES user_agent(id);
ALTER TABLE log_banner_category
    ADD CONSTRAINT log_banner_category_log_fk FOREIGN KEY(log_id) REFERENCES log(id);
ALTER TABLE banner_category
    ADD CONSTRAINT log_banner_category_category_id FOREIGN KEY(category_id) REFERENCES category(id);