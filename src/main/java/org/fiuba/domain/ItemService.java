package org.fiuba.domain;

import org.fiuba.presentation.ItemCreationBody;

public interface ItemService {
    Item create(ItemCreationBody body, Long renterId);
}
