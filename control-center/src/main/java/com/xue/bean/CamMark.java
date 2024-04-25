package com.xue.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Xue
 * @create 2024-04-25 11:03
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CamMark {

    private Integer id;

    private Boolean delay = false;

    private Boolean mock = false;

    public CamMark(Integer id) {
        this.id = id;
    }

    public CamMark(Integer id, Boolean mock) {
        this.id = id;
        this.mock = mock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CamMark camMark = (CamMark) o;
        return Objects.equals(id, camMark.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
