package com.jeffrpowell.templenamepool.model;

import java.time.LocalDate;
import java.util.Objects;

/*{
	targetDate: "",
	completed: false,
	name: {
		familySearchId: "",
		pdf: null,
		ordinances: [],
		male: false
	}
	requester: {
		id: "",
		name: "",
		email: "",
		phone: ""
	}
}*/
public class CheckedOutName {
	private WardMember requester;
	private boolean completed;
	private TempleName name;
	private LocalDate targetDate;

	public CheckedOutName()
	{
	}

	public CheckedOutName(WardMember requester, boolean completed, NameSubmission name, LocalDate targetDate)
	{
		this.requester = requester;
		this.completed = completed;
		this.name = new TempleName(name.getFamilySearchId(), new byte[0], name.getRemainingOrdinances(), name.isMale());
		this.targetDate = targetDate;
	}

	public WardMember getRequester()
	{
		return requester;
	}

	public void setRequester(WardMember requester)
	{
		this.requester = requester;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public TempleName getName()
	{
		return name;
	}

	public void setName(TempleName name)
	{
		this.name = name;
	}

	public LocalDate getTargetDate()
	{
		return targetDate;
	}

	public void setTargetDate(LocalDate targetDate)
	{
		this.targetDate = targetDate;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 41 * hash + Objects.hashCode(this.requester);
		hash = 41 * hash + (this.completed ? 1 : 0);
		hash = 41 * hash + Objects.hashCode(this.name);
		hash = 41 * hash + Objects.hashCode(this.targetDate);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final CheckedOutName other = (CheckedOutName) obj;
		if (this.completed != other.completed)
		{
			return false;
		}
		if (!Objects.equals(this.requester, other.requester))
		{
			return false;
		}
		if (!Objects.equals(this.name, other.name))
		{
			return false;
		}
		return Objects.equals(this.targetDate, other.targetDate);
	}
	
}
