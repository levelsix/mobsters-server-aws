package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="TaskMapElement")
@Table(name="task_map_element")
@Proxy(lazy=true, proxyClass=ITask.class)
public class TaskMapElement extends BaseIntPersistentObject implements ITaskMapElement{

	private static final long serialVersionUID = 8026080003793182493L;

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Task.class, optional=true)
	@JoinColumn(
		name = "task_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private ITask task;

	@Column(name = "x_pos")
	private int xPos;

	@Column(name = "y_pos")
	private int yPos;
	
	@Column(name = "element")
	private String element;
	
	@Column(name = "boss")
	private boolean boss;
	
	@Column(name = "boss_img_name")
	private String bossImgName;
	
	public TaskMapElement() {
		super();
	}

	public TaskMapElement(
		ITask task,
		int xPos,
		int yPos,
		String element,
		boolean boss,
		String bossImgName )
	{
		super();
		this.task = task;
		this.xPos = xPos;
		this.yPos = yPos;
		this.element = element;
		this.boss = boss;
		this.bossImgName = bossImgName;
	}

	@Override
	public ITask getTask()
	{
		return task;
	}

	@Override
	public void setTask( ITask task )
	{
		this.task = task;
	}

	@Override
	public int getxPos()
	{
		return xPos;
	}

	@Override
	public void setxPos( int xPos )
	{
		this.xPos = xPos;
	}

	@Override
	public int getyPos()
	{
		return yPos;
	}

	@Override
	public void setyPos( int yPos )
	{
		this.yPos = yPos;
	}

	@Override
	public String getElement()
	{
		return element;
	}

	@Override
	public void setElement( String element )
	{
		this.element = element;
	}

	@Override
	public boolean isBoss()
	{
		return boss;
	}

	@Override
	public void setBoss( boolean boss )
	{
		this.boss = boss;
	}

	@Override
	public String getBossImgName()
	{
		return bossImgName;
	}

	@Override
	public void setBossImgName( String bossImgName )
	{
		this.bossImgName = bossImgName;
	}

	@Override
	public String toString()
	{
		return "TaskMapElement [task="
			+ task
			+ ", xPos="
			+ xPos
			+ ", yPos="
			+ yPos
			+ ", element="
			+ element
			+ ", boss="
			+ boss
			+ ", bossImgName="
			+ bossImgName
			+ "]";
	}

}
