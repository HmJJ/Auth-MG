package com.springboot.code.vo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import lombok.Data;

/**
 * 分页都用这个
 * @param <T>
 */
@SuppressWarnings("hiding")
@Data
public class ReturnPageVO< T > implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8662354374597890391L;
	private int number;
	private int size;
	private int totalPages;
	private int numberOfElements;
	private long totalElements;
	private boolean hasPreviousPage;
	private boolean isFirstPage;
	private boolean hasNextPage;
	private boolean isLastPage;
	private Iterator< T > iterator;
	private List< T > content;
	private boolean hasContent;
}
