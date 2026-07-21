package org.northernarc.loanmanagementproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedData<T> {
    private List<T> items;
    private PageMeta page;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageMeta {
        private int number;
        private int size;
        private long totalElements;
        private int totalPages;
    }
}
