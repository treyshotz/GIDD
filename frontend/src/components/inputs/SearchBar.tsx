import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Activity } from 'types/Types';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { PopperPlacementType } from '@material-ui/core/Popper';
import DatePicker from 'components/inputs/DatePicker';
import Bool from 'components/inputs/Bool';
import { Button, Fade, Popper, IconButton, InputBase } from '@material-ui/core';

// Icons
import FilterIcon from '@material-ui/icons/TuneRounded';
import SearchIcon from '@material-ui/icons/SearchRounded';

// Project components
import Paper from 'components/layout/Paper';

const useStyles = makeStyles((theme) => ({
  root: {
    padding: theme.spacing(0.25, 0.5),
    display: 'flex',
    alignItems: 'center',
    width: '100%',
    borderRadius: 25,
  },
  input: {
    marginLeft: theme.spacing(1),
    flex: 1,
  },
  iconButton: {
    padding: 10,
  },
  divider: {
    height: 28,
    margin: 4,
  },
  paper: {
    border: '1px solid',
    padding: theme.spacing(1),
    marginTop: theme.spacing(1),
    borderRadius: theme.shape.borderRadius,
    width: '100%',
    backgroundColor: theme.palette.background.paper,
  },
  level: {
    display: 'flex',
    flexDirection: 'column',
  },
  rowTwo: {
    display: 'flex',
  },
  grid: {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    gap: theme.spacing(1),
  },
}));

type FormValues = Pick<Activity, 'title' | 'description' | 'endDate' | 'startDate' | 'signupStart' | 'signupEnd' | 'capacity'>;

export default function SearchBar() {
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null);
  const [open, setOpen] = useState(false);
  const [placement, setPlacement] = useState<PopperPlacementType>();
  const { control, formState } = useForm<FormValues>();

  const handleClick = (newPlacement: PopperPlacementType) => (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
    setOpen((prev) => placement !== newPlacement || !prev);
    setPlacement(newPlacement);
  };
  const id = open ? 'transitions-popper' : undefined;

  return (
    <Paper className={classes.root}>
      <IconButton aria-label='menu' className={classes.iconButton} onClick={handleClick('bottom-end')}>
        <FilterIcon />
      </IconButton>
      <Popper anchorEl={anchorEl} id={id} open={open} placement={placement} transition>
        {({ TransitionProps }) => (
          <Fade {...TransitionProps} timeout={350}>
            <div className={classes.paper}>
              <div className={classes.grid}>
                <DatePicker control={control} formState={formState} label='Start' name='startDate' rules={{ required: 'Feltet er påkrevd' }} type='date-time' />
                <DatePicker control={control} formState={formState} label='Slutt' name='endDate' rules={{ required: 'Feltet er påkrevd' }} type='date-time' />
              </div>
              <div className={classes.rowTwo}>
                <div className={classes.level}>
                  <Bool control={control} formState={formState} label='Høy' name='level_high' type='checkbox' />
                  <Bool control={control} formState={formState} label='Middels' name='level_med' type='checkbox' />
                  <Bool control={control} formState={formState} label='Lav' name='level_low' type='checkbox' />
                </div>
              </div>
              <div className={classes.grid}>
                <Button>Nullstill Filtre</Button>
                <Button>Bruk Filtre</Button>
              </div>
            </div>
          </Fade>
        )}
      </Popper>
      <InputBase className={classes.input} inputProps={{ 'aria-label': 'search google maps' }} placeholder='Søk etter aktivitet' />
      <IconButton aria-label='search' className={classes.iconButton} type='submit'>
        <SearchIcon />
      </IconButton>
    </Paper>
  );
}
