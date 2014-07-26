package com.lvl6.mobsters.tests.fixture.dummy;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.google.common.base.Preconditions;
import com.lvl6.mobsters.common.utils.AbstractAction;
import com.lvl6.mobsters.common.utils.AbstractService;
import com.lvl6.mobsters.common.utils.IRunnableAction;

public class DummyRunnableAction 
	extends AbstractAction
	implements IRunnableAction 
{
	@NotNull
	@Length(min=32, max=32)
	@Pattern(regexp="[a-f0-9]*")
	private String id;
	
	@Min(4)
	@Max(16)
	private int size;
	
	@Length(min=3, max=128)
	@Pattern(regexp="[A-Za-z,;?.! ]*")
	private String title;

	public DummyRunnableAction( AbstractService parentService, String id, int size, String title ) 
	{
		super(parentService);
		this.id = id;
		this.size = size;
		this.title = title;
	}

	@Override
	public void execute() {
		Preconditions.checkState( !failedSyntaxCheck(), "Cannot execute after failing to verify argument syntax checks" );
		
		return;
	}
}
