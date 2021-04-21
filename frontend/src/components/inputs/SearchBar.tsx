import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Activity } from 'types/Types';
import { TrainingLevel } from 'types/Enums';
import { traningLevelToText } from 'utils';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import MenuItem from '@material-ui/core/MenuItem';
import { Typography, Button, IconButton, InputBase, Collapse, Divider } from '@material-ui/core';

// Icons
import FilterIcon from '@material-ui/icons/TuneRounded';
import SearchIcon from '@material-ui/icons/SearchRounded';

// Project components
import { ActivityFilters } from 'containers/Activities';
import DatePicker from 'components/inputs/DatePicker';
import Select from 'components/inputs/Select';
import Paper from 'components/layout/Paper';
import SubmitButton from 'components/inputs/SubmitButton';

const useStyles = makeStyles((theme) => ({
  paper: {
    borderRadius: 20,
    padding: theme.spacing(0.25, 0.5),
    overflow: 'hidden',
  },
  root: {
    display: 'flex',
    alignItems: 'center',
    width: '100%',
  },
  input: {
    marginLeft: theme.spacing(1),
    flex: 1,
  },
  iconButton: {
    padding: theme.spacing(1),
  },
  filterPaper: {
    padding: theme.spacing(1),
    backgroundColor: theme.palette.background.paper,
  },
  level: {
    display: 'flex',
    flexDirection: 'column',
  },
  grid: {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    gap: theme.spacing(1),
  },
}));

type FormValues = Partial<Pick<Activity, 'title'>> & {
  endDate?: Date;
  startDate?: Date;
  level?: Activity['level'] | '';
};

export type SearchBarProps = {
  updateFilters: (newFilters: ActivityFilters) => void;
};

const SearchBar = ({ updateFilters }: SearchBarProps) => {
  const classes = useStyles();
  const [open, setOpen] = useState(false);
  const { reset, register, handleSubmit, control, formState } = useForm<FormValues>();

  const submit = async (data: FormValues) => {
    setOpen(false);
    const filters: ActivityFilters = {};
    if (data.endDate) {
      filters.startDateBefore = data.endDate.toJSON();
    }
    if (data.startDate) {
      filters.startDateAfter = data.startDate.toJSON();
    }
    if (data.title) {
      filters.title = data.title;
    }
    if (data.level) {
      filters.level = data.level;
    }
    updateFilters(filters);
  };

  const resetFilters = async () => {
    setOpen(false);
    reset({
      title: '',
      level: '',
      endDate: undefined,
      startDate: undefined,
    });
    updateFilters({});
  };

  const { ref: titleRef, name: titleName } = register('title');

  return (
    <Paper className={classes.paper} noPadding>
      <form onSubmit={handleSubmit(submit)}>
        <div className={classes.root}>
          <IconButton aria-label='menu' className={classes.iconButton} onClick={() => setOpen((prev) => !prev)}>
            <FilterIcon />
          </IconButton>
          <InputBase className={classes.input} inputRef={titleRef} name={titleName} placeholder='Søk etter aktivitet' />
          <IconButton className={classes.iconButton} type='submit'>
            <SearchIcon />
          </IconButton>
        </div>
        <Collapse in={open}>
          <Divider />
          <div className={classes.filterPaper}>
            <Typography variant='h3'>Filtre</Typography>
            <div className={classes.grid}>
              <DatePicker control={control} formState={formState} label='Start' margin='dense' name='startDate' type='date-time' />
              <DatePicker control={control} formState={formState} label='Slutt' margin='dense' name='endDate' type='date-time' />
            </div>
            <Select control={control} formState={formState} label='Trenings-nivå' name='level'>
              {Object.values(TrainingLevel).map((value, index) => (
                <MenuItem key={index} value={value}>
                  {traningLevelToText(value as TrainingLevel)}
                </MenuItem>
              ))}
            </Select>
            <div className={classes.grid}>
              <Button onClick={resetFilters} variant='outlined'>
                Nullstill filtre
              </Button>
              <SubmitButton formState={formState}>Aktiver filtre</SubmitButton>
            </div>
          </div>
        </Collapse>
      </form>
    </Paper>
  );
};

export default SearchBar;
